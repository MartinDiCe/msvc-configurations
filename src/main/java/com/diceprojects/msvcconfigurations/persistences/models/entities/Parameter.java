package com.diceprojects.msvcconfigurations.persistences.models.entities;

import com.diceprojects.msvcconfigurations.persistences.models.enums.ParameterCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;


/**
 * Representa un parámetro global del sistema.
 * <p>
 * Estos parámetros se utilizan para configurar valores globales como el timeout total,
 * umbrales del circuit breaker, etc. Se debe asegurar la unicidad del nombre del parámetro
 * a nivel de base de datos.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("parameters")
public class Parameter extends AuditableEntity {

    /**
     * Identificador único del parámetro.
     * Se genera un UUID de forma predeterminada.
     */
    @Id
    private String parameterId = UUID.randomUUID().toString();

    /**
     * Nombre del parámetro (por ejemplo, "GlobalTimeoutSeconds").
     */
    @NotNull(message = "El nombre del parámetro no puede ser nulo")
    @NotEmpty(message = "El nombre del parámetro no puede estar vacío")
    @Size(max = 100, message = "El nombre del parámetro debe tener máximo 100 caracteres")
    private String parameterName = "";

    /**
     * Valor del parámetro, en forma de cadena.
     * Se puede parsear al tipo adecuado según el parámetro.
     */
    @NotNull(message = "El valor del parámetro no puede ser nulo")
    @NotEmpty(message = "El valor del parámetro no puede estar vacío")
    private String parameterValue = "";

    /**
     * Descripción del parámetro.
     */
    @Size(max = 500, message = "La descripción debe tener máximo 500 caracteres")
    private String parameterDescription;

    /**
     * Categoría del parámetro.
     */
    private ParameterCategory parameterCategory;
}
