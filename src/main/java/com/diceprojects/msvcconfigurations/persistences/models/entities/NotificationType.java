package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa un tipo de notificación en el sistema.
 * <p>
 * Permite categorizar las notificaciones (por ejemplo, EMAIL, SMS, PUSH) y almacenar
 * una descripción del tipo.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("notificationTypes")
public class NotificationType extends AuditableEntity {

    /**
     * Identificador único del tipo de notificación.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Nombre del tipo de notificación (por ejemplo, "Email").
     */
    private String name;

    /**
     * Descripción o detalles adicionales del tipo de notificación.
     */
    private String description;
}
