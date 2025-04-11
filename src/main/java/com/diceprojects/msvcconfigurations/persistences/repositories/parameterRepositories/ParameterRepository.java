package com.diceprojects.msvcconfigurations.persistences.repositories.parameterRepositories;

import com.diceprojects.msvcconfigurations.persistences.models.entities.Parameter;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo para la entidad {@link Parameter}.
 * <p>
 * Se encarga de la interacción con la base de datos, combinando las operaciones CRUD básicas
 * (heredadas de ReactiveCrudRepository) y las operaciones personalizadas definidas en {@link ParameterRepositoryCustom}.
 * Además, la consulta por nombre es insensible a mayúsculas utilizando LOWER() en SQL.
 * </p>
 */
public interface ParameterRepository extends ReactiveCrudRepository<Parameter, String>, ParameterRepositoryCustom {

    /**
     * Busca un parámetro por su nombre de forma insensible a mayúsculas.
     *
     * @param parameterName el nombre del parámetro a buscar.
     * @return un {@link Mono} que emite el parámetro encontrado o vacío si no existe.
     */
    @Query("SELECT * FROM parameters WHERE LOWER(parameterName) = LOWER(:parameterName)")
    Mono<Parameter> findByParameterName(@Param("parameterName") String parameterName);
}
