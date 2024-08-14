package com.diceprojects.msvcconfigurations.services;

import com.diceprojects.msvcconfigurations.persistences.models.entities.Parameter;
import com.diceprojects.msvcconfigurations.persistences.repositories.ParameterRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementación del servicio para manejar los parámetros del sistema.
 */
@Service
public class ParameterServiceImpl implements ParameterService {

    private final ParameterRepository parameterRepository;

    public ParameterServiceImpl(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    @Override
    public Mono<Parameter> getParameterByName(String parameterName) {
        return parameterRepository.findByParameterName(parameterName)
                .next();
    }

    @Override
    public Mono<Parameter> saveOrUpdateParameter(Parameter parameter) {
        return parameterRepository.save(parameter);
    }

    @Override
    public Mono<Void> deleteParameterById(String parameterId) {
        return parameterRepository.deleteById(parameterId);
    }

    @Override
    public Flux<Parameter> getAllParameters() {
        return parameterRepository.findAll();
    }
}
