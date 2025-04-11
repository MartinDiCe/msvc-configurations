package com.diceprojects.msvcconfigurations.middleware;

import com.diceprojects.msvcconfigurations.helpers.CachedBodyServerHttpRequest;
import com.diceprojects.msvcconfigurations.logging.AppLogger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Filtro web que cachea el cuerpo de la solicitud (request body) para que pueda
 * ser reutilizado en otros puntos de la cadena de filtros (por ejemplo, para registros o validaciones).
 * <p>
 * Se utiliza para unir los fragmentos del cuerpo en un único DataBuffer, extraer la cadena y almacenarlo
 * en los atributos del ServerWebExchange. Luego, se reconstruye el request utilizando la clase CachedBodyServerHttpRequest.
 * </p>
 *
 * @see CachedBodyServerHttpRequest
 * @see DataBufferUtils
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CachedBodyFilter implements WebFilter {

    private final AppLogger appLogger;

    /**
     * Constructor con inyección de dependencias para el logger.
     *
     * @param appLogger la instancia de {@link AppLogger} utilizada para registrar mensajes.
     */
    public CachedBodyFilter(AppLogger appLogger) {
        this.appLogger = appLogger;
    }

    /**
     * Intercepta el flujo de la solicitud para cachear el cuerpo y reconstruir el exchange
     * con el request modificado que retorna el body cacheado.
     *
     * @param exchange el {@link ServerWebExchange} actual.
     * @param chain    la cadena de filtros a ejecutar.
     * @return un {@link Mono} que indica la finalización del procesamiento del filtro.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        appLogger.debug("Iniciando CachedBodyFilter para cachear el request body.");

        return DataBufferUtils.join(exchange.getRequest().getBody())
                .defaultIfEmpty(exchange.getResponse().bufferFactory().wrap(new byte[0]))
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);

                    String cachedBody = new String(bytes, StandardCharsets.UTF_8);
                    appLogger.debug("Request body cacheado: {}", cachedBody);

                    exchange.getAttributes().put("cachedRequestBody", cachedBody);

                    // Crea un Flux que emite el DataBuffer con el contenido cacheado.
                    Flux<DataBuffer> cachedFlux = Flux.just(
                            exchange.getResponse().bufferFactory().wrap(bytes)
                    );

                    // Construye un nuevo ServerHttpRequest que utiliza el body cacheado (pasando AppLogger).
                    ServerHttpRequest mutatedRequest = new CachedBodyServerHttpRequest(exchange.getRequest(), cachedFlux, appLogger);
                    ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                    appLogger.info("Exchange mutado con request body cacheado.");

                    return chain.filter(mutatedExchange);
                });
    }
}
