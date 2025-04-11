package com.diceprojects.msvcconfigurations.middleware;

import com.diceprojects.msvcconfigurations.helpers.ResponseCaptureDecorator;
import com.diceprojects.msvcconfigurations.logging.AppLogger;
import com.diceprojects.msvcconfigurations.persistences.models.entities.ApiTrace;
import com.diceprojects.msvcconfigurations.persistences.models.entities.Parameter;
import com.diceprojects.msvcconfigurations.services.parameterServices.ParameterService;
import com.diceprojects.msvcconfigurations.traces.ApiTraceBus;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * Middleware para capturar y publicar trazas de la comunicación de la API.
 * <p>
 * Este filtro intercepta cada solicitud y respuesta para extraer la información relevante:
 * <ul>
 *   <li>Endpoint, método, origen y los cuerpos (body) de la request y de la response.</li>
 *   <li>Tiempo de ejecución, tamaño del payload y código de respuesta HTTP.</li>
 * </ul>
 * La traza se captura y se envía a través del {@link ApiTraceBus} para su persistencia asíncrona,
 * pero solo si el parámetro "ApiTraceActive" está configurado en "true".
 * <p>
 * Se ignoran las solicitudes a endpoints del sistema (por ejemplo, Swagger o los propios endpoints
 * de trazas) para evitar capturar información irrelevante o generar bucles.
 * </p>
 * <p>
 * Los logs se realizan utilizando la instancia de {@link AppLogger}.
 * </p>
 *
 * @see ResponseCaptureDecorator
 * @see ApiTraceBus
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1) // Se ejecuta después de CachedBodyFilter
public class ApiTraceWebFilter implements WebFilter {

    private final ParameterService parameterService;
    private final AppLogger appLogger;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param parameterService el servicio de parámetros para consultar la configuración de trazas.
     * @param appLogger        el logger personalizado para registrar mensajes.
     */
    public ApiTraceWebFilter(ParameterService parameterService, AppLogger appLogger) {
        this.parameterService = parameterService;
        this.appLogger = appLogger;
    }

    /**
     * Intercepta la ejecución de la cadena de filtros para capturar y publicar la traza de la API.
     * Se consulta el parámetro "ApiTraceActive" para determinar si se debe capturar la traza.
     *
     * @param exchange el contexto de la solicitud y la respuesta.
     * @param chain    la cadena de filtros.
     * @return un {@code Mono<Void>} que indica la finalización del proceso.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        appLogger.info("Verificando si el registro de trazas está activo (ApiTraceActive)...");
        return parameterService.getParameterByName("ApiTraceActive")
                .defaultIfEmpty(new Parameter() {{
                    setParameterValue("false");
                }})
                .flatMap(param -> {
                    if (!"true".equalsIgnoreCase(param.getParameterValue())) {
                        appLogger.info("ApiTraceActive es false: se omite la captura de trazas.");
                        return chain.filter(exchange);
                    }
                    appLogger.info("ApiTraceActive es true: se procederá a capturar y publicar la traza.");
                    return captureAndPublishTrace(exchange, chain);
                })
                .doOnError(e -> appLogger.error("Error en el filtro ApiTraceWebFilter", e));
    }

    /**
     * Captura el contenido del cuerpo de la request (previamente cacheado) y decora la response para capturar
     * su body a medida que se escribe. Una vez finalizada la escritura, se calcula el tiempo de ejecución y se crea
     * un objeto {@code ApiTrace} que se publica a través del {@code ApiTraceBus}.
     *
     * @param exchange el contexto actual de la solicitud.
     * @param chain    la cadena de filtros.
     * @return un {@code Mono<Void>} que indica la finalización de la captura y publicación de la traza.
     */
    private Mono<Void> captureAndPublishTrace(ServerWebExchange exchange, WebFilterChain chain) {
        // Si la solicitud es para endpoints que no se desean capturar, por ejemplo Swagger o Auto-Traces, se ignora.
        final ServerHttpRequest request = exchange.getRequest();
        final String requestUri = request.getURI().toString();
        if (requestUri.contains("/api/apitraces") || requestUri.toLowerCase().contains("swagger") || requestUri.toLowerCase().contains("apidoc")) {
            appLogger.debug("Se detectó endpoint de sistema (apitraces, swagger, etc.): se omite captura de traza.");
            return chain.filter(exchange);
        }

        // Capturamos datos de la request.
        final String method = request.getMethod() != null ? request.getMethod().name() : "UNKNOWN";
        final String origin = request.getHeaders().getFirst("X-Forwarded-For") != null
                ? request.getHeaders().getFirst("X-Forwarded-For")
                : (request.getRemoteAddress() != null
                ? request.getRemoteAddress().getAddress().getHostAddress() : "Unknown");

        // Obtención del body cacheado depositado previamente por CachedBodyFilter.
        String requestBody = (String) exchange.getAttribute("cachedRequestBody");
        if (requestBody == null) {
            requestBody = "";
        }
        appLogger.debug("Request body capturado: {}", requestBody);

        // Decora la response para capturar su body
        final ServerHttpResponse originalResponse = exchange.getResponse();
        final ResponseCaptureDecorator decoratedResponse = new ResponseCaptureDecorator(originalResponse, appLogger);
        final ServerWebExchange mutatedExchange = exchange.mutate().response(decoratedResponse).build();

        // Medición del tiempo de ejecución
        final LocalDateTime timestamp = LocalDateTime.now();
        final long start = System.currentTimeMillis();

        final String finalRequestBody = requestBody;
        return chain.filter(mutatedExchange)
                .doFinally(signalType -> publishTrace(requestUri, method, origin, timestamp, finalRequestBody,
                        decoratedResponse, System.currentTimeMillis() - start));
    }

    /**
     * Construye y publica una traza de la API utilizando la información recopilada.
     *
     * @param requestUri        la URL del endpoint solicitado.
     * @param method            el método HTTP.
     * @param origin            el origen de la solicitud.
     * @param timestamp         la marca de tiempo de la solicitud.
     * @param requestBody       el cuerpo de la request (cacheado).
     * @param decoratedResponse la respuesta decorada de la cual se extraerá el body.
     * @param elapsedMs         el tiempo de ejecución en milisegundos.
     */
    private void publishTrace(String requestUri,
                              String method,
                              String origin,
                              LocalDateTime timestamp,
                              String requestBody,
                              ResponseCaptureDecorator decoratedResponse,
                              long elapsedMs) {
        final double executionTimeSeconds = elapsedMs / 1000.0;
        final String responsePayload = decoratedResponse.getCapturedResponseBody();
        appLogger.debug("Response body capturado: {}", responsePayload);

        int status = (decoratedResponse.getStatusCode() != null)
                ? decoratedResponse.getStatusCode().value() : 0;
        // Nota: Se calcula el tamaño del payload en bytes utilizando UTF-8.
        int payloadSize = responsePayload.getBytes(StandardCharsets.UTF_8).length;

        ApiTrace trace = new ApiTrace();
        trace.setServiceName(requestUri);
        trace.setHttpMethod(method);
        trace.setRequestOrigin(origin);
        trace.setRequestTimestamp(timestamp);
        trace.setRequestPayload(requestBody);
        trace.setResponsePayload(responsePayload);
        trace.setExecutionTimeSeconds(executionTimeSeconds);
        trace.setPayloadSize(payloadSize);
        trace.setHttpResponseCode(status);

        // Se publica la traza para su persistencia asíncrona.
        ApiTraceBus.publish(trace);
        appLogger.info("Trama API publicada: {} {} con código de respuesta {}", method, requestUri, status);
    }
}
