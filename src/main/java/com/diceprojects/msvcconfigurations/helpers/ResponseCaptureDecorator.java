package com.diceprojects.msvcconfigurations.helpers;

import com.diceprojects.msvcconfigurations.logging.AppLogger;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Decorador para {@link ServerHttpResponse} que captura el body de la respuesta de forma no intrusiva.
 * <p>
 * Esta clase intercepta cada {@link DataBuffer} que se escribe en la respuesta y, sin modificar
 * el flujo original, extrae su contenido utilizando un {@link ByteBuffer} de solo lectura. El contenido
 * se acumula en un {@link StringBuilder} para su posterior procesamiento (por ejemplo, para almacenar trazas,
 * auditorías, etc.).
 * </p>
 * <p>
 * Se aplica el principio de responsabilidad única, ya que su única función es capturar el contenido del body,
 * y se implementa de forma que se extienda sin necesidad de modificar el código existente (principio abierto/cerrado).
 * </p>
 * <p>
 * Esta implementación es 100% reactiva y utiliza el {@link AppLogger} para registrar los eventos de captura
 * y posibles errores sin interrumpir el flujo de respuesta.
 * </p>
 *
 * @see ServerHttpResponseDecorator
 * @see DataBufferUtils
 * @see AppLogger
 */
public class ResponseCaptureDecorator extends ServerHttpResponseDecorator {

    private final AppLogger logger;
    private final StringBuilder bodyBuilder = new StringBuilder();

    /**
     * Constructor que recibe la respuesta original a decorar y la instancia de {@link AppLogger} para el registro de logs.
     *
     * @param delegate La respuesta original que se decorará.
     * @param logger   La instancia de {@link AppLogger} utilizada para registrar mensajes de depuración y error.
     */
    public ResponseCaptureDecorator(ServerHttpResponse delegate, AppLogger logger) {
        super(delegate);
        this.logger = logger;
        this.logger.debug("ResponseCaptureDecorator inicializado.");
    }

    /**
     * Devuelve el contenido acumulado del body de la respuesta.
     * <p>
     * Este método retorna una cadena que puede ser vacía en caso de que no se haya capturado contenido.
     * </p>
     *
     * @return Una cadena con el contenido completo del body capturado.
     */
    public String getCapturedResponseBody() {
        String captured = bodyBuilder.toString();
        logger.debug("Response body capturado completo: {}", captured);
        return captured;
    }

    /**
     * Intercepta la escritura del body cuando se emite como un {@link Publisher} de {@link DataBuffer}.
     * <p>
     * El método se encarga de transformar el contenido del body, capturándolo y acumulándolo en
     * {@code bodyBuilder} sin interrumpir el flujo original; devolviendo el mismo buffer.
     * </p>
     *
     * @param body Un {@link Publisher} que emite objetos {@link DataBuffer} representando el body.
     * @return Un {@link Mono<Void>} que representa la finalización de la escritura del body decorado.
     */
    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        logger.debug("Iniciando captura del response body (writeWith).");
        return super.writeWith(Flux.from(body).map(this::captureBuffer));
    }

    /**
     * Intercepta la escritura del body cuando se utiliza el método {@code writeAndFlushWith(...)}.
     * <p>
     * Este método aplana el flujo de publishers y, para cada {@link DataBuffer}, captura su contenido
     * y lo acumula en {@code bodyBuilder}. Se devuelve el buffer original para conservar el flujo de respuesta.
     * </p>
     *
     * @param body Un {@link Publisher} de publishers de {@link DataBuffer}.
     * @return Un {@link Mono<Void>} que representa la finalización de la escritura del body decorado.
     */
    @Override
    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        logger.debug("Iniciando captura del response body (writeAndFlushWith).");
        Flux<Publisher<? extends DataBuffer>> fluxBody = Flux.from(body);
        Flux<DataBuffer> flattened = fluxBody.flatMap(inner ->
                Flux.from(inner).flatMap(dataBuffer -> Mono.just(captureBuffer(dataBuffer)))
        );
        return super.writeAndFlushWith(flattened.map(Flux::just));
    }

    /**
     * Método auxiliar que se encarga de capturar el contenido del {@link DataBuffer}.
     * <p>
     * Retiene el buffer para evitar que se libere prematuramente y extrae su contenido en forma de String
     * que se acumula en {@code bodyBuilder}. En caso de error, se registra el mismo y se devuelve el buffer original.
     * </p>
     *
     * @param original El {@link DataBuffer} original a capturar.
     * @return El mismo {@link DataBuffer} original para que continúe en el flujo.
     */
    private DataBuffer captureBuffer(DataBuffer original) {
        try {
            // Retener el DataBuffer para evitar que sea liberado antes de tiempo
            DataBufferUtils.retain(original);
            // Crear un ByteBuffer de solo lectura a partir del original
            ByteBuffer readOnly = original.asByteBuffer().asReadOnlyBuffer();
            byte[] bytes = new byte[readOnly.remaining()];
            readOnly.get(bytes);
            String chunk = new String(bytes, StandardCharsets.UTF_8);
            bodyBuilder.append(chunk);
            logger.debug("Chunk capturado: {}", chunk);
        } catch (Exception ex) {
            logger.error("Error al capturar chunk del response body.", ex);
        }
        return original;
    }
}
