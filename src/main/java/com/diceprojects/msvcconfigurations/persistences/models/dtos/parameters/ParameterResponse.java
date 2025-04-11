package com.diceprojects.msvcconfigurations.persistences.models.dtos.parameters;

import lombok.Data;

/**
 * DTO de respuesta para representar un parámetro.
 */
@Data
public class ParameterResponse {

    private String parameterId;
    private String parameterName;
    private String parameterValue;
    private String parameterDescription;
    private String parameterCategory;
}
