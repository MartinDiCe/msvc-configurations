package com.diceprojects.msvcconfigurations.middleware;

import com.diceprojects.msvcconfigurations.exceptions.ResourceNotFoundException;
import com.diceprojects.msvcconfigurations.logging.AppLogger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

/**
 * Controlador global de excepciones que intercepta las excepciones lanzadas en los controladores
 * y las traduce en respuestas HTTP estandarizadas.
 * <p>
 * Se manejan diferentes tipos de errores:
 * <ul>
 *   <li>{@code IllegalArgumentException}: Error en la solicitud del cliente (400 Bad Request).</li>
 *   <li>{@code ResourceNotFoundException}: Recurso no encontrado (404 Not Found).</li>
 *   <li>{@code RuntimeException} y {@code Exception}: Otros errores internos (500 Internal Server Error).</li>
 *   <li>{@code WebExchangeBindException}: Errores de validación de los argumentos del request (400 Bad Request).</li>
 * </ul>
 * Los errores se registran mediante {@link AppLogger} para mantener el comportamiento definido para cada perfil (dev/prod).
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final AppLogger appLogger;

    /**
     * Constructor con inyección del AppLogger.
     *
     * @param appLogger el logger a utilizar.
     */
    public GlobalExceptionHandler(AppLogger appLogger) {
        this.appLogger = appLogger;
    }

    /**
     * {@inheritDoc}
     *
     * Maneja excepciones de tipo {@code IllegalArgumentException} y devuelve un error 400 (Bad Request).
     *
     * @param ex la excepción capturada.
     * @return un {@link Mono} que emite un objeto {@link ErrorResponse} con el código 400 y el mensaje de error.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        appLogger.error("IllegalArgumentException capturada", ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return Mono.just(errorResponse);
    }

    /**
     * {@inheritDoc}
     *
     * Maneja excepciones de tipo {@code ResourceNotFoundException} y devuelve un error 404 (Not Found).
     *
     * @param ex la excepción capturada.
     * @return un {@link Mono} que emite un objeto {@link ErrorResponse} con el código 404 y el mensaje de error.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        appLogger.error("ResourceNotFoundException capturada", ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return Mono.just(errorResponse);
    }

    /**
     * {@inheritDoc}
     *
     * Maneja excepciones de tipo {@code RuntimeException} y devuelve un error 500 (Internal Server Error).
     *
     * @param ex la excepción capturada.
     * @return un {@link Mono} que emite un objeto {@link ErrorResponse} con el código 500 y el mensaje de error.
     */
    @ExceptionHandler(RuntimeException.class)
    public Mono<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        appLogger.error("RuntimeException capturada", ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return Mono.just(errorResponse);
    }

    /**
     * {@inheritDoc}
     *
     * Maneja cualquier otra excepción y devuelve un error 500 (Internal Server Error).
     *
     * @param ex la excepción capturada.
     * @return un {@link Mono} que emite un objeto {@link ErrorResponse} con el código 500 y un mensaje de error.
     */
    @ExceptionHandler(Exception.class)
    public Mono<ErrorResponse> handleException(Exception ex) {
        appLogger.error("Excepción capturada", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor: " + ex.getMessage()
        );
        return Mono.just(errorResponse);
    }

    /**
     * Maneja las excepciones de validación que se producen cuando un request no cumple con las restricciones
     * definidas en los DTOs (por ejemplo, {@code @NotNull}, {@code @NotEmpty}, {@code @Pattern}, etc.).
     * <p>
     * Este método captura específicamente las excepciones de tipo {@link WebExchangeBindException}, recorre los errores de validación
     * y construye un mensaje formateado que incluye el nombre del campo y el mensaje de error correspondiente.
     * Se devuelve un objeto {@link ErrorResponse} con el código HTTP 400 (Bad Request) y el mensaje detallado.
     * </p>
     *
     * @param ex la excepción de validación capturada.
     * @return un {@link Mono} que emite un objeto {@link ErrorResponse} con el código 400 y el mensaje de error.
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ErrorResponse> handleValidationException(WebExchangeBindException ex) {
        StringBuilder errorMessage = new StringBuilder("Errores de validación: ");
        ex.getFieldErrors().forEach(error ->
                errorMessage.append(String.format("[%s: %s] ", error.getField(), error.getDefaultMessage()))
        );
        appLogger.error("Excepción de validación capturada", ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage.toString().trim());
        return Mono.just(errorResponse);
    }
}
