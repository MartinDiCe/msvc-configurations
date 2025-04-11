package com.diceprojects.msvcconfigurations.persistences.repositories.parameterRepositories;

import com.diceprojects.msvcconfigurations.persistences.models.entities.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Implementación de las operaciones personalizadas definidas en {@link ParameterRepositoryCustom}
 * para la entidad {@link Parameter}.
 * <p>
 * Esta clase se encarga exclusivamente de la interacción con la base de datos para operaciones
 * personalizadas, como el borrado lógico y la restauración.
 * </p>
 */
@Repository
public class ParameterRepositoryCustomImpl implements ParameterRepositoryCustom {

    private final R2dbcEntityTemplate template;

    @Autowired
    public ParameterRepositoryCustomImpl(R2dbcEntityTemplate template) {
        this.template = template;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> logicDelete(String id) {
        return template.getDatabaseClient()
                .sql("UPDATE parameters SET eliminado = true WHERE parameterId = $1")
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
                .sql("UPDATE parameters SET eliminado = false WHERE parameterId = $1")
                .bind("$1", id)
                .fetch()
                .rowsUpdated()
                .then();
    }
}
