package com.diceprojects.msvccompany.Logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Implementación del logger para ambiente de desarrollo.
 * <p>
 * Utiliza SLF4J para registrar los mensajes de log en diferentes niveles.
 * </p>
 */
@Profile("dev")
@Component
public class DevAppLogger implements AppLogger {

    private final Logger logger = LoggerFactory.getLogger(DevAppLogger.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String message, Object... args) {
        logger.debug(message, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String message, Throwable t, Object... args) {
        logger.error(message, args, t);
    }
}
