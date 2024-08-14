package com.diceprojects.msvcconfigurations.services;

import com.diceprojects.msvcconfigurations.persistences.models.entities.Parameter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz del servicio para manejar los parámetros del sistema.
 */
public interface ParameterService {

    /**
     * Obtiene un parámetro por su nombre.
     *
     * @param parameterName el nombre del parámetro
     * @return un Mono que emite el parámetro encontrado
     */
    Mono<Parameter> getParameterByName(String parameterName);

    /**
     * Crea o actualiza un parámetro.
     *
     * @param parameter el parámetro a crear o actualizar
     * @return un Mono que emite el parámetro guardado
     */
    Mono<Parameter> saveOrUpdateParameter(Parameter parameter);

    /**
     * Elimina un parámetro por su ID.
     *
     * @param parameterId el ID del parámetro a eliminar
     * @return un Mono que emite vacío cuando se completa la eliminación
     */
    Mono<Void> deleteParameterById(String parameterId);

    /**
     * Obtiene todos los parámetros.
     *
     * @return un Flux que emite todos los parámetros
     */
    Flux<Parameter> getAllParameters();

}
