package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa una ciudad en el sistema.
 * <p>
 * Almacena información básica sobre la ciudad, como su nombre y una descripción opcional.
 * Además, extiende {@link AuditableEntity} para incorporar información de auditoría y gestión de borrado lógico.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("cities")
public class City extends AuditableEntity {

    /**
     * Identificador único de la ciudad.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Nombre de la ciudad.
     */
    private String name;

    /**
     * Descripción o notas adicionales sobre la ciudad.
     */
    private String description;

    /**
     * Identificador del estado o provincia al que pertenece la ciudad (opcional).
     */
    private String stateId;
}
