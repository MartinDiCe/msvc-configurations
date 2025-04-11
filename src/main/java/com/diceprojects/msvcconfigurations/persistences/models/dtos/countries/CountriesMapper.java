package com.diceprojects.msvcconfigurations.persistences.models.dtos.countries;

import com.diceprojects.msvcconfigurations.persistences.models.entities.Countries;

/**
 * Mapper para convertir entre los DTOs de solicitud/respuesta y la entidad Countries.
 */
public class CountriesMapper {

    /**
     * Convierte un DTO CountriesCreateRequest en una entidad Countries.
     * <p>
     * En creación, el id no se envía y la entidad lo generará automáticamente (por ejemplo, con UUID).
     * Se convierte el campo codePhone de String a int.
     * </p>
     *
     * @param request el DTO de creación.
     * @return la entidad Countries.
     */
    public static Countries toEntity(CountriesCreateRequest request) {
        Countries country = new Countries();
        country.setCountryName(request.getName());
        country.setCountryCode(request.getCode());
        country.setCountryCodePhone(Integer.parseInt(request.getCodePhone()));
        country.setCountryDescription(request.getDescription());
        country.setCountryTimeZone(request.getTimeZone());
        return country;
    }

    /**
     * Actualiza de forma parcial una entidad Countries a partir de un DTO CountriesUpdateRequest.
     * Solo se actualizan los campos que estén presentes (no nulos y no vacíos) en el DTO.
     *
     * @param request el DTO de actualización.
     * @param existing la entidad Countries existente.
     * @return la entidad Countries actualizada.
     */
    public static Countries updateEntity(CountriesUpdateRequest request, Countries existing) {
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            existing.setCountryName(request.getName());
        }
        if (request.getCode() != null && !request.getCode().trim().isEmpty()) {
            existing.setCountryCode(request.getCode());
        }
        if (request.getCodePhone() != null && !request.getCodePhone().trim().isEmpty()) {
            existing.setCountryCodePhone(Integer.parseInt(request.getCodePhone()));
        }
        if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            existing.setCountryDescription(request.getDescription());
        }
        if (request.getTimeZone() != null && !request.getTimeZone().trim().isEmpty()) {
            existing.setCountryTimeZone(request.getTimeZone());
        }

        return existing;
    }

    /**
     * Convierte una entidad Countries en un DTO CountriesResponse.
     *
     * @param country la entidad Countries.
     * @return el DTO de respuesta.
     */
    public static CountriesResponse toResponse(Countries country) {
        CountriesResponse response = new CountriesResponse();
        response.setId(country.getCountryId());
        response.setName(country.getCountryName());
        response.setCode(country.getCountryCode());
        response.setCodePhone(country.getCountryCodePhone());
        response.setDescription(country.getCountryDescription());
        response.setTimeZone(country.getCountryTimeZone());
        return response;
    }
}
