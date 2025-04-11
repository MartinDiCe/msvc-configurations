package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa una plantilla para notificaciones en el sistema.
 * <p>
 * Almacena el nombre, asunto y cuerpo de la notificación, así como una referencia
 * al tipo de notificación que se aplicará.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("notificationTemplates")
public class NotificationTemplate extends AuditableEntity {

    /**
     * Identificador único de la plantilla de notificación.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Nombre de la plantilla.
     */
    private String name;

    /**
     * Asunto de la notificación.
     */
    private String subject;

    /**
     * Cuerpo o contenido de la notificación.
     */
    private String body;

    /**
     * Identificador del tipo de notificación asociado.
     */
    private String notificationTypeId;
}
