package com.diceprojects.msvcconfigurations.persistences.models.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Representa un país en el sistema.
 * <p>
 * Esta entidad almacena la información básica de un país, como su nombre, código ISO, código telefónico y una descripción.
 * Además, extiende {@link AuditableEntity} para incorporar información de auditoría y borrado lógico.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("countries")
public class Countries extends AuditableEntity {

    /**
     * Identificador único del país.
     */
    @Id
    private String countryId = UUID.randomUUID().toString();

    /**
     * Nombre del país.
     */
    private String countryName;

    /**
     * Código ISO o abreviatura del país.
     */
    private String countryCode;

    /**
     * Código telefónico internacional del país.
     */
    @NotNull(message = "El código telefónico es obligatorio")
    @NotEmpty(message = "El código telefónico no puede estar vacío")
    @Pattern(regexp = "^[1-9]\\d*$", message = "El código telefónico es inválido. No debe tener ceros a la izquierda")
    private int countryCodePhone;

    /**
     * Descripción adicional o notas sobre el país.
     */
    private String countryDescription;

    /**
     * Zona Horaria del País.
     */
    private String countryTimeZone;

}
