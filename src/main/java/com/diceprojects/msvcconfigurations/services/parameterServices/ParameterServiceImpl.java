package com.diceprojects.msvcconfigurations.services.parameterServices;

import com.diceprojects.msvcconfigurations.logging.AppLogger;
import com.diceprojects.msvcconfigurations.persistences.models.entities.Parameter;
import com.diceprojects.msvcconfigurations.persistences.repositories.parameterRepositories.ParameterRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementación del servicio para la entidad {@link Parameter}.
 * <p>
 * Se encarga de orquestar las operaciones de negocio utilizando el repositorio y el R2dbcEntityTemplate para la comunicación
 * con la base de datos. Implementa lógica para manejo de errores, logging y validación.
 * </p>
 */
@Service
public class ParameterServiceImpl implements ParameterService {

    private final ParameterRepository parameterRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final AppLogger appLogger;

    public ParameterServiceImpl(ParameterRepository parameterRepository,
                                R2dbcEntityTemplate r2dbcEntityTemplate,
                                AppLogger appLogger) {
        this.parameterRepository = parameterRepository;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
        this.appLogger = appLogger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Parameter> getParameterByName(String parameterName) {
        appLogger.debug("Buscando parámetro por nombre: {}", parameterName);
        return parameterRepository.findByParameterName(parameterName)
                .doOnSuccess(param -> {
                    if (param != null) {
                        appLogger.info("Parámetro encontrado: {}", param.getParameterName());
                    } else {
                        appLogger.warn("No se encontró parámetro con nombre: {}", parameterName);
                    }
                })
                .doOnError(e -> appLogger.error("Error al buscar parámetro con nombre: {}", e, parameterName))
                .onErrorMap(e -> new RuntimeException("Error en la búsqueda del parámetro: " + parameterName, e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Parameter> getParameterById(String id) {
        appLogger.debug("Obteniendo parámetro con id: {}", id);
        return parameterRepository.findById(id)
                .doOnSuccess(param -> {
                    if (param != null) {
                        appLogger.info("Parámetro encontrado: {}", param.getParameterName());
                    } else {
                        appLogger.warn("No se encontró parámetro con id: {}", id);
                    }
                })
                .doOnError(e -> appLogger.error("Error al obtener parámetro con id: {}", e, id))
                .onErrorMap(e -> new RuntimeException("Error al obtener parámetro con id: " + id, e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Parameter> getAllParameters() {
        appLogger.debug("Obteniendo todos los parámetros");
        return parameterRepository.findAll()
                .doOnComplete(() -> appLogger.info("Se obtuvieron todos los parámetros"))
                .doOnError(e -> appLogger.error("Error al obtener los parámetros", e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Parameter> saveOrUpdateParameter(Parameter parameter) {
        appLogger.debug("Guardando/Actualizando parámetro (updateOrCreate): {}", parameter.getParameterName());
        return parameterRepository.findById(parameter.getParameterId())
                .flatMap(existing ->
                        // Si el parámetro ya existe, se actualiza
                        parameterRepository.save(parameter)
                )
                .switchIfEmpty(
                        // Si no existe, se inserta utilizando R2dbcEntityTemplate
                        r2dbcEntityTemplate.insert(Parameter.class)
                                .using(parameter)
                )
                .doOnSuccess(saved -> appLogger.info("Parámetro guardado/actualizado: {}", saved.getParameterName()))
                .doOnError(e -> appLogger.error("Error al guardar/actualizar parámetro: {}", e, parameter.getParameterName()))
                .onErrorMap(e ->
                        new RuntimeException("Error al guardar/actualizar el parámetro: " + parameter.getParameterName(), e)
                );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> deleteParameter(String id) {
        appLogger.debug("Eliminando parámetro con id: {}", id);
        return parameterRepository.deleteById(id)
                .doOnSuccess(v -> appLogger.info("Parámetro eliminado con éxito, id: {}", id))
                .doOnError(e -> appLogger.error("Error al eliminar parámetro con id: {}", e, id))
                .onErrorMap(e -> new RuntimeException("Error al eliminar parámetro con id: " + id, e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> logicDeleteParameter(String id) {
        appLogger.debug("Marcando como eliminado lógicamente el parámetro con id: {}", id);
        return parameterRepository.logicDelete(id)
                .doOnSuccess(v -> appLogger.info("Parámetro marcado como eliminado lógicamente, id: {}", id))
                .doOnError(e -> appLogger.error("Error en borrado lógico del parámetro con id: {}", e, id))
                .onErrorMap(e -> new RuntimeException("Error en borrado lógico del parámetro con id: " + id, e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> restoreParameter(String id) {
        appLogger.debug("Restaurando parámetro con id: {}", id);
        return parameterRepository.restore(id)
                .doOnSuccess(v -> appLogger.info("Parámetro restaurado con éxito, id: {}", id))
                .doOnError(e -> appLogger.error("Error al restaurar parámetro con id: {}", e, id))
                .onErrorMap(e -> new RuntimeException("Error al restaurar el parámetro con id: " + id, e));
    }

    /**
     * {@inheritDoc}
     */
    public Mono<Parameter> createParameter(Parameter parameter) {
        appLogger.debug("Creando parámetro: {}", parameter.getParameterName());
        // Se fuerza a que el id sea nulo para permitir que se genere un nuevo UUID en la entidad
        parameter.setParameterId(null);
        return r2dbcEntityTemplate.insert(Parameter.class)
                .using(parameter)
                .doOnSuccess(saved -> appLogger.info("Parámetro creado: {}", saved.getParameterName()))
                .onErrorMap(e -> new RuntimeException("Error al crear el parámetro: " + parameter.getParameterName(), e));
    }

    /**
     * {@inheritDoc}
     */
    public Mono<Parameter> updateParameter(Parameter parameter) {
        appLogger.debug("Actualizando parámetro de forma parcial: {}", parameter.getParameterName());
        return getParameterById(parameter.getParameterId())
                .flatMap(existing -> {
                    boolean changed = false;
                    if (parameter.getParameterName() != null && !parameter.getParameterName().trim().isEmpty()
                            && !parameter.getParameterName().equals(existing.getParameterName())) {
                        existing.setParameterName(parameter.getParameterName());
                        changed = true;
                    }
                    if (parameter.getParameterValue() != null && !parameter.getParameterValue().trim().isEmpty()
                            && !parameter.getParameterValue().equals(existing.getParameterValue())) {
                        existing.setParameterValue(parameter.getParameterValue());
                        changed = true;
                    }
                    if (parameter.getParameterDescription() != null && !parameter.getParameterDescription().trim().isEmpty()
                            && !parameter.getParameterDescription().equals(existing.getParameterDescription())) {
                        existing.setParameterDescription(parameter.getParameterDescription());
                        changed = true;
                    }
                    if (parameter.getParameterCategory() != null
                            && parameter.getParameterCategory() != existing.getParameterCategory()) {
                        existing.setParameterCategory(parameter.getParameterCategory());
                        changed = true;
                    }
                    if (changed) {
                        return parameterRepository.save(existing)
                                .doOnSuccess(updated -> appLogger.info("Parámetro actualizado: {}", updated.getParameterName()));
                    } else {
                        appLogger.info("No se detectaron cambios en el parámetro: {}", existing.getParameterName());
                        return Mono.just(existing);
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró el parámetro con id: " + parameter.getParameterId())))
                .onErrorMap(e -> new RuntimeException("Error al actualizar el parámetro: " + parameter.getParameterName(), e));
    }
}
