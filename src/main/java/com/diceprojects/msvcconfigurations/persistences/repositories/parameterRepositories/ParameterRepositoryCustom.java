package com.diceprojects.msvcconfigurations.persistences.repositories.parameterRepositories;

import com.diceprojects.msvcconfigurations.persistences.models.entities.Parameter;
import reactor.core.publisher.Mono;

/**
 * Interfaz para operaciones personalizadas en el repositorio de la entidad {@link Parameter}.
 * <p>
 * Define métodos para el borrado lógico y la restauración de parámetros.
 * </p>
 */
public interface ParameterRepositoryCustom {

    /**
     * Marca un parámetro como eliminado lógicamente.
     *
     * @param id el identificador del parámetro.
     * @return un {@link Mono} que señala la finalización de la operación.
     */
    Mono<Void> logicDelete(String id);

    /**
     * Restaura un parámetro marcado como eliminado lógicamente.
     *
     * @param id el identificador del parámetro.
     * @return un {@link Mono} que señala la finalización de la operación.
     */
    Mono<Void> restore(String id);
}
