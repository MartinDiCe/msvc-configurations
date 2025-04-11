package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa un método de pago disponible en el sistema.
 * <p>
 * Define los distintos métodos de pago (como tarjeta de crédito, PayPal, etc.) y permite
 * gestionar su estado (habilitado o deshabilitado) y detalles adicionales.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("paymentMethods")
public class PaymentMethod extends AuditableEntity {

    /**
     * Identificador único del método de pago.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Nombre del método de pago (por ejemplo, "Credit Card").
     */
    private String name;

    /**
     * Descripción o detalles adicionales del método de pago.
     */
    private String description;

    /**
     * Indica si el método de pago está habilitado.
     */
    private boolean enabled;
}
