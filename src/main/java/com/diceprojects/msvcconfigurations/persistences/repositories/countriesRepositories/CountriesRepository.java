package com.diceprojects.msvcconfigurations.persistences.repositories.countriesRepositories;

import com.diceprojects.msvcconfigurations.persistences.models.entities.Countries;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo para la entidad {@link Countries}.
 * <p>
 * Se encarga únicamente de la interacción con la base de datos, delegando en el repositorio custom
 * para operaciones específicas.
 * </p>
 */
public interface CountriesRepository extends ReactiveCrudRepository<Countries, String>, CountriesRepositoryCustom {

    /**
     * Busca un país por su nombre.
     *
     * @param name el nombre del país a buscar.
     * @return un {@link Mono} que emite el país encontrado o vacío si no existe.
     */
    @Query("SELECT * FROM countries WHERE LOWER(name) = LOWER(:name)")
    Mono<Countries> findByName(@Param("name") String name);
}
