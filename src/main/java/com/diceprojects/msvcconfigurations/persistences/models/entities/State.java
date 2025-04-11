package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa un estado o provincia en el sistema.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("states")
public class State extends AuditableEntity {

    /**
     * Identificador único del estado/provincia.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Nombre del estado o provincia.
     */
    private String name;

    /**
     * Código ISO o abreviatura, si aplica.
     */
    private String code;

    /**
     * Descripción o notas adicionales.
     */
    private String description;

    /**
     * Identificador del país al que pertenece.
     */
    private String countryId;
}
