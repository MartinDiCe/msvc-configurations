package com.diceprojects.msvccompany.Logging;

/**
 * Interfaz que define las operaciones de logging.
 */
public interface AppLogger {

    /**
     * Registra un mensaje en nivel DEBUG.
     *
     * @param message el mensaje a registrar.
     * @param args argumentos opcionales para el mensaje.
     */
    void debug(String message, Object... args);

    /**
     * Registra un mensaje en nivel INFO.
     *
     * @param message el mensaje a registrar.
     * @param args argumentos opcionales para el mensaje.
     */
    void info(String message, Object... args);

    /**
     * Registra un mensaje en nivel WARN.
     *
     * @param message el mensaje a registrar.
     * @param args argumentos opcionales para el mensaje.
     */
    void warn(String message, Object... args);

    /**
     * Registra un mensaje en nivel ERROR.
     *
     * @param message el mensaje a registrar.
     * @param t la excepción que se desea registrar.
     * @param args argumentos opcionales para el mensaje.
     */
    void error(String message, Throwable t, Object... args);
}
