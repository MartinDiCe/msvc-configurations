package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa un barrio en el sistema.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("neighborhoods")
public class Neighborhood extends AuditableEntity {

    /**
     * Identificador único del barrio.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Nombre del barrio.
     */
    private String name;

    /**
     * Identificador de la localidad a la que pertenece.
     */
    private String localityId;
}
