package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa la información específica de un teléfono.
 * <p>
 * Almacena el número de teléfono, una posible extensión y otros datos relacionados.
 * Además, extiende {@link AuditableEntity} para incorporar información de auditoría.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("phone")
public class Phone extends AuditableEntity {

    /**
     * Identificador único del registro de teléfono.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Número de teléfono.
     */
    private String phoneNumber;

    /**
     * Extensión del teléfono, si aplica.
     */
    private String extension;

    /**
     * Tipo de teléfono, que se validará contra los valores permitidos definidos en el parámetro "PhoneType".
     * Por ejemplo: "WHATSAPP", "TRABAJO", "CELULAR".
     */
    private String phoneType;

    /**
     * Identificador del país asociado al teléfono.
     * Se relaciona con la entidad {@link Countries}.
     */
    private String countryId;

}
