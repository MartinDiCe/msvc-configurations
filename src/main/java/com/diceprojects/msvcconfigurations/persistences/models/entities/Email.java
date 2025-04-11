package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa la información específica de un correo electrónico.
 * <p>
 * Almacena la dirección de correo y un indicador de verificación (por ejemplo, para confirmar si el email fue validado).
 * Además, extiende {@link AuditableEntity} para incorporar información de auditoría.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("email")
public class Email extends AuditableEntity {

    /**
     * Identificador único del registro de email.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Dirección de correo electrónico.
     */
    private String emailAddress;

    /**
     * Indica si el correo electrónico ha sido verificado.
     */
    private boolean verified;
}
