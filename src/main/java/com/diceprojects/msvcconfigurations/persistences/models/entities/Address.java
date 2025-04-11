package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa la información específica de un domicilio o dirección.
 * <p>
 * Almacena la calle, ciudad, código postal, estado, país, latitud, longitud y otros datos
 * que facilitan la normalización y geocodificación. Además, extiende {@link AuditableEntity}
 * para incorporar información de auditoría.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("address")
public class Address extends AuditableEntity {

    /**
     * Identificador único del registro de dirección.
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Calle y número de la dirección.
     */
    private String street;

    /**
     * Ciudad de la dirección.
     */
    private String cityId;

    /**
     * Código postal.
     */
    private String postalCode;

    /**
     * Identificador del estado o provincia al que pertenece la dirección.
     */
    private String stateId;

    /**
     * Identificador del país al que pertenece la dirección.
     */
    private String countryId;

    /**
     * Latitud de la dirección (en formato decimal).
     */
    private Double latitude;

    /**
     * Longitud de la dirección (en formato decimal).
     */
    private Double longitude;

    /**
     * Barrio o distrito, si aplica.
     */
    private String neighborhood;

    /**
     * Dirección formateada, de acuerdo con la normalización o respuesta del servicio de geocodificación.
     */
    private String formattedAddress;

    /**
     * Datos de normalización en formato JSON obtenidos de un servicio de mapas.
     * Almacena la respuesta completa del servicio de geocodificación para análisis o validaciones posteriores.
     */
    private String normalizationData;

    /**
     * Entrecalles u otras referencias adicionales para ubicar la dirección.
     */
    private String crossStreets;

    /**
     * Observaciones o notas adicionales sobre la dirección.
     */
    private String observations;

}
