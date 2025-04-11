package com.diceprojects.msvcconfigurations.persistences.repositories.countriesRepository;

import com.diceprojects.msvcconfigurations.persistences.models.entities.Countries;
import reactor.core.publisher.Mono;

/**
 * Interfaz para operaciones personalizadas en el repositorio de la entidad {@link Countries}.
 * <p>
 * Se definen métodos que requieren consultas SQL específicas que no están cubiertas
 * por las operaciones CRUD básicas.
 * </p>
 */
public interface CountriesRepositoryCustom {

    /**
     * Busca un país por su código ISO o abreviatura.
     *
     * @param code el código del país a buscar.
     * @return un {@link Mono} que emite el país encontrado o vacío si no existe.
     */
    Mono<Countries> findByCode(String code);

    /**
     * Marca un país como eliminado lógicamente.
     * <p>
     * Este método actualiza el campo de borrado lógico (por ejemplo, 'eliminado') a true.
     * </p>
     *
     * @param id el identificador del país a marcar como eliminado.
     * @return un {@link Mono} que señala la finalización de la operación.
     */
    Mono<Void> logicDelete(String id);

    /**
     * Restaura un país previamente marcado como eliminado lógicamente.
     * <p>
     * Este método actualiza el campo de borrado lógico a false.
     * </p>
     *
     * @param id el identificador del país a restaurar.
     * @return un {@link Mono} que señala la finalización de la operación.
     */
    Mono<Void> restore(String id);

}
