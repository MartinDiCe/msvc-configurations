package com.diceprojects.msvcconfigurations.persistences.models.entities;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa la configuración de impuestos en el sistema.
 * <p>
 * Permite definir la tasa impositiva, el nombre del impuesto (por ejemplo, "VAT"),
 * el país al que se aplica el impuesto y, opcionalmente, el estado o provincia.
 * Se garantiza la integridad referencial a nivel de país mediante el campo {@code countryId}.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("taxSettings")
public class TaxSetting extends AuditableEntity {

    /**
     * Identificador único de la configuración de impuestos.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Nombre de la configuración de impuestos (por ejemplo, "VAT").
     */
    private String name;

    /**
     * Tasa de impuesto en porcentaje (por ejemplo, 21.0 para 21%).
     */
    private Double rate;

    /**
     * Identificador del país al que se aplica el impuesto.
     * Este campo se relaciona con la entidad {@link Countries} y es obligatorio.
     */
    @NotNull(message = "El identificador del país (countryId) es obligatorio")
    private String countryId;

    /**
     * Identificador del estado o provincia al que se aplica el impuesto.
     * Este campo es opcional y permite afinar la configuración impositiva.
     */
    private String stateId;

    /**
     * Descripción o detalles adicionales sobre la configuración.
     */
    private String description;
}
