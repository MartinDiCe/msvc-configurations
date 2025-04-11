package com.diceprojects.msvcconfigurations.logging;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementación del logger para ambiente de producción.
 * <p>
 * Esta clase implementa la interfaz {@link AppLogger} para el entorno de producción.
 * En este perfil se suprimen los logs de nivel {@code debug}, {@code info} y {@code warn},
 * y sólo se registran los errores utilizando {@code System.err}.
 * </p>
 *
 * @see AppLogger
 */
@Profile("prod")
@Component
public class ProdAppLogger implements AppLogger {

    private static final Logger logger = Logger.getLogger(ProdAppLogger.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String message, Object... args) {
        // No se registran logs debug en producción.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String message, Object... args) {
        // No se registran logs info en producción.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String message, Object... args) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String message, Throwable t, Object... args) {
        String formattedMessage = String.format(message, args);
        logger.log(Level.SEVERE, formattedMessage, t);
    }
}
