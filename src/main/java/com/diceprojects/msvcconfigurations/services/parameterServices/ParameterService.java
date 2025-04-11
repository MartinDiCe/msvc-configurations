package com.diceprojects.msvcconfigurations.services.parameterServices;

import com.diceprojects.msvcconfigurations.persistences.models.entities.Parameter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz del servicio para la entidad {@link Parameter}.
 * <p>
 * Define las operaciones de negocio para gestionar parámetros globales,
 * incluyendo las operaciones básicas (crear, actualizar, buscar y eliminar) y
 * las operaciones personalizadas (borrado lógico y restauración).
 * Este servicio actúa como intermediario entre los controladores y el repositorio.
 * </p>
 */
public interface ParameterService {

    /**
     * Busca un parámetro por su nombre.
     *
     * @param parameterName el nombre del parámetro.
     * @return un {@link Mono} que emite el parámetro encontrado o vacío si no existe.
     */
    Mono<Parameter> getParameterByName(String parameterName);

    /**
     * Busca un parámetro por su identificador.
     *
     * @param id el identificador del parámetro.
     * @return un {@link Mono} que emite el parámetro encontrado o vacío si no existe.
     */
    Mono<Parameter> getParameterById(String id);

    /**
     * Obtiene todos los parámetros.
     *
     * @return un {@link Flux} que emite todos los parámetros.
     */
    Flux<Parameter> getAllParameters();

    /**
     * Guarda o actualiza un parámetro de forma genérica (utilizado en DataInitializer).
     *
     * @param parameter el parámetro a guardar o actualizar.
     * @return un {@link Mono} que emite el parámetro guardado o actualizado.
     */
    Mono<Parameter> saveOrUpdateParameter(Parameter parameter);

    /**
     * Crea un nuevo parámetro.
     * Se asume que en la creación no se proporciona un id (el mismo se genera automáticamente).
     *
     * @param parameter el parámetro a crear.
     * @return un {@link Mono} que emite el parámetro creado.
     */
    Mono<Parameter> createParameter(Parameter parameter);

    /**
     * Actualiza de forma parcial un parámetro.
     * Se conservan los campos existentes que no son enviados en la solicitud.
     *
     * @param parameter el parámetro con el id y los campos a actualizar.
     * @return un {@link Mono} que emite el parámetro actualizado o el mismo si no se detectaron cambios.
     */
    Mono<Parameter> updateParameter(Parameter parameter);

    /**
     * Elimina físicamente un parámetro.
     *
     * @param id el identificador del parámetro a eliminar.
     * @return un {@link Mono} que señala la finalización de la operación.
     */
    Mono<Void> deleteParameter(String id);

    /**
     * Marca un parámetro como eliminado lógicamente.
     *
     * @param id el identificador del parámetro.
     * @return un {@link Mono} que señala la finalización de la operación.
     */
    Mono<Void> logicDeleteParameter(String id);

    /**
     * Restaura un parámetro marcado como eliminado lógicamente.
     *
     * @param id el identificador del parámetro.
     * @return un {@link Mono} que señala la finalización de la operación.
     */
    Mono<Void> restoreParameter(String id);
}
