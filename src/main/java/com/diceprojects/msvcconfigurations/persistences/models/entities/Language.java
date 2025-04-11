package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa un idioma disponible en el sistema.
 * <p>
 * Permite gestionar la internacionalización del sistema, almacenando el nombre del idioma,
 * su código ISO y una descripción adicional.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("languages")
public class Language extends AuditableEntity {

    /**
     * Identificador único del idioma.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Nombre del idioma (por ejemplo, "English").
     */
    private String name;

    /**
     * Código ISO del idioma (por ejemplo, "en").
     */
    private String isoCode;

    /**
     * Descripción o notas adicionales sobre el idioma.
     */
    private String description;
}
