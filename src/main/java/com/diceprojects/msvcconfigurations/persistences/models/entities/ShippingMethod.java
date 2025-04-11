package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa un método de envío disponible en el sistema.
 * <p>
 * Define modalidades de envío, incluyendo el nombre, costo, tiempo estimado de entrega
 * y una descripción opcional.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("shippingMethods")
public class ShippingMethod extends AuditableEntity {

    /**
     * Identificador único del método de envío.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Nombre del método de envío (por ejemplo, "Standard Shipping").
     */
    private String name;

    /**
     * Costo del envío.
     */
    private Double cost;

    /**
     * Tiempo estimado de entrega en días.
     */
    private Integer estimatedDeliveryDays;

    /**
     * Descripción o detalles adicionales del método de envío.
     */
    private String description;
}
