package com.diceprojects.msvcconfigurations.middleware;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO para representar la respuesta de error en las peticiones HTTP.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    /**
     * Código de estado HTTP.
     */
    private int status;

    /**
     * Mensaje descriptivo del error.
     */
    private String message;
}
