package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa una localidad en el sistema.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("localities")
public class Locality extends AuditableEntity {

    /**
     * Identificador único de la localidad.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Nombre de la localidad.
     */
    private String name;

    /**
     * Identificador del estado/provincia al que pertenece.
     */
    private String stateId;

    /**
     * Código postal de la localidad.
     */
    private String postalCode;

}
