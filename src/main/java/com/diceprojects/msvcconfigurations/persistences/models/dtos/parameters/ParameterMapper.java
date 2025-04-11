package com.diceprojects.msvcconfigurations.persistences.models.dtos.parameters;

import com.diceprojects.msvcconfigurations.persistences.models.entities.Parameter;
import com.diceprojects.msvcconfigurations.persistences.models.enums.ParameterCategory;

public class ParameterMapper {

    /**
     * Convierte un ParameterRequest en una entidad Parameter.
     * Se asume que el campo parameterCategory en el DTO es un String que coincide con el nombre del enum.
     *
     * @param request el DTO de solicitud.
     * @return la entidad Parameter.
     */
    public static Parameter toEntity(ParameterRequest request) {
        Parameter parameter = new Parameter();
        if (request.getParameterId() != null && !request.getParameterId().trim().isEmpty()) {
            parameter.setParameterId(request.getParameterId());
        }
        parameter.setParameterName(request.getParameterName());
        parameter.setParameterValue(request.getParameterValue());
        parameter.setParameterDescription(request.getParameterDescription());
        if(request.getParameterCategory() != null) {
            try {
                parameter.setParameterCategory(ParameterCategory.valueOf(request.getParameterCategory().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Categoría de parámetro inválida: " + request.getParameterCategory(), e);
            }
        }
        return parameter;
    }

    /**
     * Convierte una entidad Parameter en un DTO de respuesta.
     *
     * @param parameter la entidad Parameter.
     * @return el DTO de respuesta.
     */
    public static ParameterResponse toResponse(Parameter parameter) {
        ParameterResponse response = new ParameterResponse();
        response.setParameterId(parameter.getParameterId());
        response.setParameterName(parameter.getParameterName());
        response.setParameterValue(parameter.getParameterValue());
        response.setParameterDescription(parameter.getParameterDescription());
        response.setParameterCategory(parameter.getParameterCategory() != null ? parameter.getParameterCategory().name() : null);
        return response;
    }
}
