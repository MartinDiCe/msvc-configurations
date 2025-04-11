package com.diceprojects.msvcconfigurations.persistences.models.enums;

/**
 * Enumeración de categorías para los parámetros globales del sistema.
 * <p>
 * Estas categorías permiten clasificar los parámetros según su funcionalidad o área de aplicación.
 * Por ejemplo:
 * <ul>
 *   <li>SYSTEM: Parámetros generales del sistema.</li>
 *   <li>TIMEOUT: Parámetros relacionados con tiempos de espera.</li>
 *   <li>SECURITY: Parámetros relacionados con la seguridad.</li>
 *   <li>NETWORK: Parámetros relacionados con la configuración de red.</li>
 *   <li>LOGGING: Parámetros relacionados con el registro de logs.</li>
 * </ul>
 * </p>
 */
public enum ParameterCategory {
    SYSTEM,
    TIMEOUT,
    SECURITY,
    NETWORK,
    LOGGING
}
