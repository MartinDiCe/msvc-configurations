package com.diceprojects.msvcconfigurations.persistences.models.dtos.parameters;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de solicitud para crear o actualizar un parámetro.
 */
@Data
public class ParameterRequest {

    /**
     * Identificador del parámetro.
     * Opcional en creación, requerido en actualizaciones.
     */
    private String parameterId;

    /**
     * Nombre del parámetro (por ejemplo, "TokenTimeOut", "EmailDomains").
     */
    @NotNull(message = "El nombre del parámetro es obligatorio")
    @NotEmpty(message = "El nombre del parámetro no puede estar vacío")
    @Size(max = 100, message = "El nombre debe tener máximo 100 caracteres")
    private String parameterName;

    /**
     * Valor del parámetro.
     */
    @NotNull(message = "El valor del parámetro es obligatorio")
    @NotEmpty(message = "El valor del parámetro no puede estar vacío")
    private String parameterValue;

    /**
     * Descripción del parámetro.
     */
    @Size(max = 500, message = "La descripción debe tener máximo 500 caracteres")
    private String parameterDescription;

    /**
     * Categoría del parámetro.
     * Se espera un valor que corresponda a uno de los parámetros del sistema (por ejemplo, SYSTEM, TIMEOUT, etc.).
     */
    @NotNull(message = "La categoría del parámetro es obligatoria")
    private String parameterCategory;
}
