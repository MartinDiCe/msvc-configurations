package com.diceprojects.msvcconfigurations.services.apiTraceServices;

import com.diceprojects.msvcconfigurations.persistences.models.entities.ApiTrace;
import com.diceprojects.msvcconfigurations.persistences.repositories.apiTraceRepositories.ApiTraceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementación del servicio para las trazas de la API.
 */
@Service
public class ApiTraceServiceImpl implements ApiTraceService {

    private final ApiTraceRepository apiTraceRepository;

    public ApiTraceServiceImpl(ApiTraceRepository apiTraceRepository) {
        this.apiTraceRepository = apiTraceRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<ApiTrace> createAsync(ApiTrace trace) {
        return apiTraceRepository.save(trace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<ApiTrace> getAllTraces() {
        return apiTraceRepository.findAll();
    }
}
