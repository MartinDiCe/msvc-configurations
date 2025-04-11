package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa un dato de contacto asociado a una entidad a nivel general.
 * <p>
 * Esta tabla fachada almacena los datos comunes a todos los tipos de contacto,
 * como el tipo de contacto (PHONE, EMAIL, ADDRESS), el tipo de entidad asociada (por ejemplo, COMPANY, CUSTOMER, PROVIDER),
 * el identificador de la entidad y una referencia (detailId) al registro en la tabla especializada correspondiente.
 * Además, extiende {@link AuditableEntity} para incorporar información de auditoría.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("contactDetail")
public class ContactDetail extends AuditableEntity {

    /**
     * Identificador único del registro de contacto en la tabla fachada.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Tipo de contacto, que debe coincidir con uno de los valores permitidos (por ejemplo, "PHONE", "EMAIL", "ADDRESS").
     */
    private String contactType;

    /**
     * Tipo de entidad asociada (por ejemplo, "COMPANY", "CUSTOMER", "PROVIDER").
     */
    private String entityType;

    /**
     * Identificador de la entidad asociada.
     */
    private String entityId;

    /**
     * Identificador del registro en la tabla especializada correspondiente (por ejemplo, en la tabla phone, email o address).
     */
    private String detailId;

    /**
     * Indica si este contacto es el principal para la entidad.
     */
    private boolean principal;
}
