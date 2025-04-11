package com.diceprojects.msvcconfigurations.services.apiTraceServices;

import com.diceprojects.msvcconfigurations.persistences.models.entities.ApiTrace;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Define las operaciones para gestionar trazas de la API.
 */
public interface ApiTraceService {

    /**
     * Persiste una nueva traza en la base de datos.
     *
     * @param trace la traza a registrar.
     * @return un {@link Mono} que emite la traza registrada.
     */
    Mono<ApiTrace> createAsync(ApiTrace trace);

    /**
     * Obtiene todas las trazas registradas.
     *
     * @return un {@link Flux} que emite todas las trazas.
     */
    Flux<ApiTrace> getAllTraces();
}
