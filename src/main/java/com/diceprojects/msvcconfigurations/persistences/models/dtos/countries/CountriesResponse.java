package com.diceprojects.msvcconfigurations.persistences.models.dtos.countries;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO de respuesta para representar un país.
 */
@Data
public class CountriesResponse {

    @Schema(example = "8f840c68-3332-4f83-8348-f48e033282b5", description = "Identificador único del país")
    private String id;

    @Schema(example = "Argentina", description = "Nombre del país")
    private String name;

    @Schema(example = "AR", description = "Código ISO del país")
    private String code;

    @Schema(example = "54", description = "Código telefónico del país")
    private Integer codePhone;

    @Schema(example = "Argentina", description = "Descripción del país")
    private String description;

    @Schema(example = "UTC-3", description = "Zona Horaria de Argentina")
    private String TimeZone;
}
