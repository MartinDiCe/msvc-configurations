package com.diceprojects.msvcconfigurations.traces;

import com.diceprojects.msvcconfigurations.logging.AppLogger;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Filtro para registrar las solicitudes entrantes y las respuestas salientes.
 * <p>
 * Este filtro se ejecuta de forma reactiva y utiliza {@link AppLogger} para registrar información clave
 * de la comunicación de la API, como el método HTTP, la URI de la solicitud y el código de estado de la respuesta.
 * </p>
 * <p>
 * <b>Uso:</b> Se registra la entrada de cada solicitud y la salida de cada respuesta para ayudar en el
 * monitoreo y la trazabilidad del comportamiento del sistema.
 * </p>
 */
@Component
public class RequestResponseLoggingFilter implements WebFilter {

    private final AppLogger appLogger;

    /**
     * Constructor que inyecta el logger personalizado.
     *
     * @param appLogger la implementación de {@link AppLogger} según el perfil activo (por ejemplo, Dev o Prod)
     */
    public RequestResponseLoggingFilter(AppLogger appLogger) {
        this.appLogger = appLogger;
    }

    /**
     * Intercepta cada solicitud/respuesta y registra la información relevante.
     *
     * @param exchange el contexto actual de la solicitud/respuesta.
     * @param chain    la cadena de filtros.
     * @return un {@link Mono<Void>} que indica cuándo ha finalizado la operación.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        appLogger.info("Incoming Request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());
        return chain.filter(exchange)
                .doOnSuccess(aVoid ->
                        appLogger.info("Outgoing Response: {}", exchange.getResponse().getStatusCode())
                );
    }
}
