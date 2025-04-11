package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa una moneda utilizada en el sistema.
 * <p>
 * Almacena información como el código ISO, nombre, símbolo y, opcionalmente,
 * una tasa de conversión respecto a una moneda base.
 * Además, extiende {@link AuditableEntity} para incorporar información de auditoría.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("currencies")
public class Currency extends AuditableEntity {

    /**
     * Identificador único de la moneda.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Código ISO de la moneda (por ejemplo, "USD").
     */
    private String code;

    /**
     * Nombre de la moneda (por ejemplo, "United States Dollar").
     */
    private String name;

    /**
     * Símbolo de la moneda (por ejemplo, "$").
     */
    private String symbol;

    /**
     * Tasa de conversión respecto a la moneda base.
     * Por ejemplo, si la moneda base es USD, para EUR podría ser 0.85.
     */
    private Double conversionRate;

    /**
     * Indica si esta moneda es la moneda base del sistema.
     * Solo debe haber una moneda marcada como base.
     */
    private boolean baseCurrency;

    /**
     * Descripción o notas adicionales sobre la moneda.
     */
    private String description;
}
