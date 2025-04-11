package com.diceprojects.msvcconfigurations.services.countriesServices;

import com.diceprojects.msvcconfigurations.logging.AppLogger;
import com.diceprojects.msvcconfigurations.persistences.models.entities.Countries;
import com.diceprojects.msvcconfigurations.persistences.repositories.countriesRepositories.CountriesRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementación del servicio para la entidad {@link Countries}.
 * <p>
 * Se encarga de orquestar las operaciones de negocio utilizando el repositorio y el
 * R2dbcEntityTemplate para la comunicación con la base de datos. Incluye manejo de errores,
 * logging y validación. Además, se implementan métodos para la creación de nuevos países
 * y la actualización parcial de los existentes.
 * </p>
 */
@Service
public class CountriesServiceImpl implements CountriesService {

    private final CountriesRepository countriesRepository;
    private final AppLogger appLogger;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param countriesRepository Repositorio para la entidad Countries.
     * @param appLogger           Componente para logging.
     * @param r2dbcEntityTemplate Template para inserciones reactivas.
     */
    public CountriesServiceImpl(CountriesRepository countriesRepository,
                                AppLogger appLogger,
                                R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.countriesRepository = countriesRepository;
        this.appLogger = appLogger;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Countries> getById(String id) {
        appLogger.debug("Obteniendo país con id: {}", id);
        return countriesRepository.findById(id)
                .doOnSuccess(country -> {
                    if (country != null) {
                        appLogger.info("País encontrado: {}", country.getCountryName());
                    } else {
                        appLogger.warn("No se encontró país con id: {}", id);
                    }
                })
                .doOnError(e -> appLogger.error("Error al obtener país con id: {}", e, id))
                .onErrorMap(e -> new RuntimeException("Error en la búsqueda por id: " + id, e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Countries> getByName(String name) {
        appLogger.debug("Obteniendo país con nombre: {}", name);
        return countriesRepository.findByName(name)
                .doOnSuccess(country -> {
                    if (country != null) {
                        appLogger.info("País encontrado: {}", country.getCountryName());
                    } else {
                        appLogger.warn("No se encontró país con nombre: {}", name);
                    }
                })
                .doOnError(e -> appLogger.error("Error al obtener país con nombre: {}", e, name))
                .onErrorMap(e -> new RuntimeException("Error en la búsqueda por nombre: " + name, e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Countries> getAll() {
        appLogger.debug("Obteniendo todos los países");
        return countriesRepository.findAll()
                .doOnComplete(() -> appLogger.info("Se obtuvieron todos los países"))
                .doOnError(e -> appLogger.error("Error al obtener todos los países", e));
    }

    /**
     * {@inheritDoc}
     *
     * Para la creación se fuerza el campo id a null, de modo que la entidad genere automáticamente un valor (por ejemplo, un UUID).
     */
    @Override
    public Mono<Countries> createCountry(Countries country) {
        appLogger.debug("Creando país: {}", country.getCountryName());
        return r2dbcEntityTemplate.insert(Countries.class)
                .using(country)
                .doOnSuccess(saved -> appLogger.info("País creado con éxito: {}", saved.getCountryName()))
                .onErrorMap(e -> new RuntimeException("Error al crear país: " + country.getCountryName(), e));
    }

    /**
     * {@inheritDoc}
     *
     * Realiza una actualización parcial: se consultan los valores existentes y se actualizan solo aquellos
     * campos que vienen en el request y que son distintos; si no se detectan cambios, se retorna el registro sin modificaciones.
     */
    @Override
    public Mono<Countries> updateCountry(Countries country) {
        appLogger.debug("Actualizando país con id: {}", country.getCountryId());
        return getById(country.getCountryId())
                .flatMap(existing -> {
                    boolean changed = false;
                    if (country.getCountryName() != null && !country.getCountryName().trim().isEmpty()
                            && !country.getCountryName().equals(existing.getCountryName())) {
                        existing.setCountryName(country.getCountryName());
                        changed = true;
                    }
                    if (country.getCountryCode() != null && !country.getCountryCode().trim().isEmpty()
                            && !country.getCountryCode().equals(existing.getCountryCode())) {
                        existing.setCountryCode(country.getCountryCode());
                        changed = true;
                    }
                    if (country.getCountryCodePhone() != existing.getCountryCodePhone()) {
                        existing.setCountryCodePhone(country.getCountryCodePhone());
                        changed = true;
                    }
                    if (country.getCountryDescription() != null && !country.getCountryDescription().trim().isEmpty()
                            && !country.getCountryDescription().equals(existing.getCountryDescription())) {
                        existing.setCountryDescription(country.getCountryDescription());
                        changed = true;
                    }
                    if (country.getCountryTimeZone() != null && !country.getCountryTimeZone().trim().isEmpty()) {
                        assert country.getCountryDescription() != null;
                        if (!country.getCountryDescription().equals(existing.getCountryTimeZone())) {
                            existing.setCountryTimeZone(country.getCountryTimeZone());
                            changed = true;
                        }
                    }
                    if (changed) {
                        return countriesRepository.save(existing)
                                .doOnSuccess(updated -> appLogger.info("País actualizado: {}", updated.getCountryName()));
                    } else {
                        appLogger.info("No se detectaron cambios en el país: {}", existing.getCountryName());
                        return Mono.just(existing);
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró el país con id: " + country.getCountryId())))
                .onErrorMap(e -> new RuntimeException("Error al actualizar país con id: " + country.getCountryId(), e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> deleteCountry(String id) {
        appLogger.debug("Eliminando país con id: {}", id);
        return countriesRepository.deleteById(id)
                .doOnSuccess(v -> appLogger.info("País eliminado con éxito: {}", id))
                .doOnError(e -> appLogger.error("Error al eliminar país con id: {}", e, id))
                .onErrorMap(e -> new RuntimeException("Error al eliminar país con id: " + id, e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> logicDeleteCountry(String id) {
        appLogger.debug("Marcando como eliminado lógicamente el país con id: {}", id);
        return countriesRepository.logicDelete(id)
                .doOnSuccess(v -> appLogger.info("País marcado como eliminado lógicamente: {}", id))
                .doOnError(e -> appLogger.error("Error en borrado lógico del país con id: {}", e, id))
                .onErrorMap(e -> new RuntimeException("Error en borrado lógico del país con id: " + id, e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> restoreCountry(String id) {
        appLogger.debug("Restaurando el país con id: {}", id);
        return countriesRepository.restore(id)
                .doOnSuccess(v -> appLogger.info("País restaurado con éxito: {}", id))
                .doOnError(e -> appLogger.error("Error al restaurar el país con id: {}", e, id))
                .onErrorMap(e -> new RuntimeException("Error al restaurar país con id: " + id, e));
    }
}
