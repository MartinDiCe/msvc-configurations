package com.diceprojects.msvcconfigurations.helpers;

import com.diceprojects.msvcconfigurations.logging.AppLogger;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

/**
 * Decorador para el {@link ServerHttpRequest} que permite reutilizar el body cacheado de la solicitud.
 * <p>
 * Esta clase encapsula el cuerpo de la solicitud en un {@link Flux} de {@link DataBuffer} para que pueda ser
 * leído múltiples veces sin consumirlo, lo cual es útil para filtros adicionales que necesiten acceder al contenido
 * del request sin interferir en el proceso original.
 * </p>
 * <p>
 * <b>Uso:</b> Instanciar este decorador proporcionando la solicitud original, el cuerpo cacheado y una instancia de {@link AppLogger}
 * para registrar los eventos relacionados con la solicitud.
 * </p>
 */
public class CachedBodyServerHttpRequest extends ServerHttpRequestDecorator {

    private final Flux<DataBuffer> cachedBody;
    private final AppLogger appLogger;

    /**
     * Constructor que recibe la solicitud original, el body cacheado y la instancia del logger.
     *
     * @param delegate   La solicitud HTTP original.
     * @param cachedBody Un {@link Flux} que provee el {@link DataBuffer} del body cacheado.
     * @param appLogger  La instancia de {@link AppLogger} para registrar los eventos de logging.
     */
    public CachedBodyServerHttpRequest(ServerHttpRequest delegate, Flux<DataBuffer> cachedBody, AppLogger appLogger) {
        super(delegate);
        this.cachedBody = cachedBody;
        this.appLogger = appLogger;
        this.appLogger.debug("CachedBodyServerHttpRequest inicializado con body cacheado.");
    }

    /**
     * Retorna el cuerpo de la solicitud cacheado.
     *
     * @return Un {@link Flux} de {@link DataBuffer} que contiene el body cacheado.
     */
    @Override
    public Flux<DataBuffer> getBody() {
        appLogger.debug("Invocado CachedBodyServerHttpRequest.getBody(), devolviendo el body cacheado.");
        return cachedBody;
    }
}
