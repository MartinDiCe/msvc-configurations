package com.diceprojects.msvcconfigurations.persistences.repositories.apiTraceRepositories;

import com.diceprojects.msvcconfigurations.persistences.models.entities.ApiTrace;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * Repositorio reactivo para la entidad {@link ApiTrace}.
 * Proporciona operaciones CRUD básicas y un método para obtener todas las trazas.
 */
public interface ApiTraceRepository extends ReactiveCrudRepository<ApiTrace, Integer> {

    /**
     * Obtiene todas las trazas registradas.
     *
     * @return un {@link Flux} que emite todas las ApiTrace.
     */
    Flux<ApiTrace> findAll();
}
