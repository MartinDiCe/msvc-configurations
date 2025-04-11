package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa un feature toggle (bandera de funcionalidad) en el sistema.
 * <p>
 * Permite habilitar o deshabilitar funcionalidades de forma dinámica, facilitando la
 * gestión de experimentos y la activación progresiva de características.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("featureToggles")
public class FeatureToggle extends AuditableEntity {

    /**
     * Identificador único del feature toggle.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Nombre de la funcionalidad o feature.
     */
    private String featureName;

    /**
     * Indica si la funcionalidad está habilitada.
     */
    private boolean enabled;

    /**
     * Descripción o detalles adicionales sobre el feature toggle.
     */
    private String description;
}
