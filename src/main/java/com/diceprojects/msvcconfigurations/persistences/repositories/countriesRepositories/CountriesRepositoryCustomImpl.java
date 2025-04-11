package com.diceprojects.msvcconfigurations.persistences.repositories.countriesRepositories;

import com.diceprojects.msvcconfigurations.persistences.models.entities.Countries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Implementación de las operaciones personalizadas definidas en {@link CountriesRepositoryCustom}
 * para la entidad {@link Countries}.
 * <p>
 * Esta clase se encarga únicamente de la interacción con la base de datos para consultas específicas,
 * sin incluir lógica de negocio.
 * </p>
 */
@Repository
public class CountriesRepositoryCustomImpl implements CountriesRepositoryCustom {

    private final R2dbcEntityTemplate template;

    /**
     * Constructor que inyecta la plantilla de R2DBC.
     *
     * @param template la plantilla de R2DBC para operaciones reactivas.
     */
    @Autowired
    public CountriesRepositoryCustomImpl(R2dbcEntityTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Countries> findByCode(String code) {
        return template.getDatabaseClient()
                .sql("SELECT * FROM countries WHERE LOWER(countryCode) = LOWER($1)")
                .bind("$1", code)
                .map((row, metadata) -> template.getConverter().read(Countries.class, row))
                .one();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> logicDelete(String id) {
        return template.getDatabaseClient()
                .sql("UPDATE countries SET eliminado = true WHERE countryId = $1")
                .bind("$1", id)
                .fetch()
                .rowsUpdated()
                .then();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> restore(String id) {
        return template.getDatabaseClient()
                .sql("UPDATE countries SET eliminado = false WHERE countryId = $1")
                .bind("$1", id)
                .fetch()
                .rowsUpdated()
                .then();
    }

}
