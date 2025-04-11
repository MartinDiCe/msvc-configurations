package com.diceprojects.msvcconfigurations.services.countriesServices;

import com.diceprojects.msvcconfigurations.persistences.models.entities.Countries;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz del servicio para la entidad {@link Countries}.
 * <p>
 * Define las operaciones de negocio para gestionar países, incluyendo:
 * <ul>
 *   <li>Operaciones de consulta: por id, por nombre y obtener todos.</li>
 *   <li>Operaciones de modificación: creación y actualización parcial.</li>
 *   <li>Operaciones de borrado: eliminación física, borrado lógico y restauración.</li>
 * </ul>
 * Este servicio actúa como intermediario entre los controladores y el repositorio,
 * utilizando un enfoque 100% reactivo (Spring WebFlux y R2DBC) para garantizar operaciones
 * no bloqueantes, escalables y con buen manejo de errores.
 * </p>
 */
public interface CountriesService {

    /**
     * Busca un país por su identificador.
     * <p>
     * Este método consulta el repositorio de manera reactiva y retorna un Mono que
     * emite el país correspondiente o un Mono vacío en caso de que no se encuentre.
     * </p>
     *
     * @param id el identificador del país.
     * @return un {@link Mono} que emite el país encontrado o vacío si no existe.
     */
    Mono<Countries> getById(String id);

    /**
     * Busca un país por su nombre.
     * <p>
     * El método realiza la búsqueda ignorando mayúsculas/minúsculas.
     * </p>
     *
     * @param name el nombre del país.
     * @return un {@link Mono} que emite el país encontrado o vacío si no existe.
     */
    Mono<Countries> getByName(String name);

    /**
     * Obtiene todos los países.
     * <p>
     * Retorna un Flux con todos los registros disponibles.
     * </p>
     *
     * @return un {@link Flux} que emite todos los países.
     */
    Flux<Countries> getAll();

    /**
     * Crea un país nuevo.
     * <p>
     * Para la creación se ignora cualquier identificador enviado; se espera que la entidad se
     * encarge de generar automáticamente el id (por ejemplo, mediante UUID).
     * </p>
     *
     * @param country el país a crear.
     * @return un {@link Mono} que emite el país creado.
     */
    Mono<Countries> createCountry(Countries country);

    /**
     * Actualiza de forma parcial un país.
     * <p>
     * Este método busca el país existente por su id y actualiza únicamente aquellos campos
     * que se envíen en el request (es decir, que sean no nulos y no vacíos). Si no se detectan
     * cambios, se retorna la entidad sin modificar.
     * </p>
     *
     * @param country el país con el id y los campos a actualizar.
     * @return un {@link Mono} que emite el país actualizado o el mismo si no hubo cambios.
     */
    Mono<Countries> updateCountry(Countries country);

    /**
     * Elimina físicamente un país.
     * <p>
     * Se realiza un borrado total del registro de la base de datos.
     * </p>
     *
     * @param id el identificador del país a eliminar.
     * @return un {@link Mono} que señala la finalización de la operación.
     */
    Mono<Void> deleteCountry(String id);

    /**
     * Marca un país como eliminado lógicamente.
     * <p>
     * Este método actualiza el registro para indicar que está eliminado, sin eliminarlo físicamente.
     * </p>
     *
     * @param id el identificador del país.
     * @return un {@link Mono} que señala la finalización de la operación.
     */
    Mono<Void> logicDeleteCountry(String id);

    /**
     * Restaura un país marcado como eliminado lógicamente.
     * <p>
     * Se actualiza el registro para quitar la marca de borrado lógico.
     * </p>
     *
     * @param id el identificador del país.
     * @return un {@link Mono} que señala la finalización de la operación.
     */
    Mono<Void> restoreCountry(String id);
}
