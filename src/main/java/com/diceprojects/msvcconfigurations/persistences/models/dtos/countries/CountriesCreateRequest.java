package com.diceprojects.msvcconfigurations.persistences.models.dtos.countries;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de solicitud para crear un país.
 */
@Data
public class CountriesCreateRequest {

    @NotNull(message = "El nombre del país es obligatorio")
    @NotEmpty(message = "El nombre del país no puede estar vacío")
    @Size(max = 100, message = "El nombre del país debe tener máximo 100 caracteres")
    @Schema(example = "Argentina", description = "Nombre del país")
    private String name;

    @NotNull(message = "El código ISO es obligatorio")
    @NotEmpty(message = "El código ISO no puede estar vacío")
    @Size(max = 10, message = "El código ISO debe tener máximo 10 caracteres")
    @Schema(example = "AR", description = "Código ISO del país")
    private String code;

    @NotNull(message = "El código telefónico es obligatorio")
    @NotEmpty(message = "El código telefónico no puede estar vacío")
    @Pattern(regexp = "^[1-9]\\d*$", message = "El código telefónico es inválido. Se espera un número Entero sin ceros a la izquierda.")
    @Schema(example = "54", description = "Código telefónico del país")
    private String codePhone;

    @Size(max = 250, message = "La descripción debe tener máximo 250 caracteres")
    @Schema(example = "Argentina", description = "Descripción del país")
    private String description;

    @NotNull(message = "La zona horaria es obligatoria")
    @NotEmpty(message = "La zona horaria no puede estar vacía")
    @Size(max = 10, message = "La zona horaria debe tener máximo 10 caracteres")
    @Schema(example = "UTC", description = "Zona horaria global del país")
    private String timeZone;
}
