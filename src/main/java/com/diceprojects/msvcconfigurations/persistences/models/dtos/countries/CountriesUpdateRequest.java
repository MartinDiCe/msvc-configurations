package com.diceprojects.msvcconfigurations.persistences.models.dtos.countries;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de solicitud para actualizar un país.
 * Se espera que se envíe el id para identificar el registro,
 * y sólo se actualizarán los campos que se envíen (actualización parcial).
 */
@Data
public class CountriesUpdateRequest {

    @NotNull(message = "El identificador del país es obligatorio para actualizar")
    @NotEmpty(message = "El identificador del país no puede estar vacío")
    @Schema(example = "8f840c68-3332-4f83-8348-f48e033282b5", description = "Identificador único del país a actualizar")
    private String id;

    @Schema(example = "Argentina", description = "Nombre del país")
    private String name;

    @Schema(example = "AR", description = "Código ISO del país")
    private String code;

    @Schema(example = "54", description = "Código telefónico del país")
    private String codePhone;

    @Schema(example = "Argentina", description = "Descripción del país")
    private String description;

    @Schema(example = "UTC", description = "Zona horaria global del país")
    private String timeZone;
}
