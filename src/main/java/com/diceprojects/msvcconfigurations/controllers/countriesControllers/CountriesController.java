package com.diceprojects.msvcconfigurations.controllers.countriesControllers;

import com.diceprojects.msvcconfigurations.persistences.models.dtos.countries.CountriesCreateRequest;
import com.diceprojects.msvcconfigurations.persistences.models.dtos.countries.CountriesMapper;
import com.diceprojects.msvcconfigurations.persistences.models.dtos.countries.CountriesResponse;
import com.diceprojects.msvcconfigurations.persistences.models.dtos.countries.CountriesUpdateRequest;
import com.diceprojects.msvcconfigurations.persistences.models.entities.Countries;
import com.diceprojects.msvcconfigurations.services.countriesServices.CountriesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador reactivo para la entidad {@link com.diceprojects.msvcconfigurations.persistences.models.entities.Countries}.
 * <p>
 * Este controlador expone endpoints para crear, actualizar (parcialmente), eliminar (física y lógicamente)
 * y consultar países, siguiendo un enfoque 100% reactivo basado en Spring WebFlux y R2DBC. Además, se
 * integra con el sistema de validación (Jakarta Bean Validation) y delega el manejo de logging y errores
 * a los servicios correspondientes.
 * </p>
 *
 * <p>
 * En nuestro enfoque se utiliza:
 * <ul>
 *   <li>Un método {@code createCountry} para insertar nuevos registros, donde se ignora el id recibido (si viene) para generar uno automáticamente.</li>
 *   <li>Un método {@code updateCountry} que realiza actualizaciones parciales: solo se actualizan los campos enviados en el request,
 *       manteniendo los datos existentes para aquellos campos no enviados (o nulos/ vacíos).</li>
 *   <li>Manejo reactivo de errores y validación, logrando un flujo no bloqueante en toda la operación.</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/countries")
public class CountriesController {

    private final CountriesService countriesService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param countriesService el servicio para gestionar países.
     */
    public CountriesController(CountriesService countriesService) {
        this.countriesService = countriesService;
    }

    /**
     * Obtiene un país a partir de su identificador.
     *
     * @param id el identificador del país.
     * @return un {@link Mono} que emite el {@link CountriesResponse} correspondiente si se encuentra.
     */
    @GetMapping("/{id}")
    public Mono<CountriesResponse> getCountryById(@PathVariable String id) {
        return countriesService.getById(id)
                .map(CountriesMapper::toResponse);
    }

    /**
     * Busca un país por su nombre, sin distinción de mayúsculas/minúsculas.
     *
     * @param name el nombre del país.
     * @return un {@link Mono} que emite el {@link CountriesResponse} si se encuentra.
     */
    @GetMapping("/byName")
    public Mono<CountriesResponse> getCountryByName(@RequestParam String name) {
        return countriesService.getByName(name)
                .map(CountriesMapper::toResponse);
    }

    /**
     * Obtiene todos los países.
     *
     * @return un {@link Flux} que emite un {@link CountriesResponse} por cada registro encontrado.
     */
    @GetMapping
    public Flux<CountriesResponse> getAllCountries() {
        return countriesService.getAll()
                .map(CountriesMapper::toResponse);
    }

    /**
     * Crea un nuevo país.
     * <p>
     * Se espera que la solicitud no incluya un identificador; en tal caso, la entidad generará un id automáticamente.
     * </p>
     *
     * @param request el DTO de solicitud con los datos del país.
     * @return un {@link Mono} que emite el {@link CountriesResponse} del país creado, con código de estado 201 (CREATED).
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CountriesResponse> createCountry(@Valid @RequestBody CountriesCreateRequest request) {
        Countries country = CountriesMapper.toEntity(request);
        return countriesService.createCountry(country)
                .map(CountriesMapper::toResponse);
    }

    /**
     * Actualiza de forma parcial un país.
     * <p>
     * Este endpoint espera el identificador dentro del objeto de solicitud para identificar el registro a actualizar.
     * Se realiza un merge de datos: se actualizan únicamente aquellos campos que vienen con valores no nulos y no vacíos.
     * Si no se detectan cambios, se retorna la entidad sin modificaciones.
     * </p>
     *
     * @param request el DTO con el id y los campos a actualizar.
     * @return un {@link Mono} que emite el {@link CountriesResponse} del país actualizado.
     */
    @PutMapping
    public Mono<CountriesResponse> updateCountry(@Valid @RequestBody CountriesUpdateRequest request) {
        return countriesService.getById(request.getId())
                .flatMap(existing -> {

                    Countries updated = CountriesMapper.updateEntity(request, existing);

                    return countriesService.updateCountry(updated);
                })
                .map(CountriesMapper::toResponse);
    }

    /**
     * Elimina físicamente un país de la base de datos.
     *
     * @param id el identificador del país a eliminar.
     * @return un {@link Mono} que indica la finalización de la operación, con código de estado 204 (NO_CONTENT).
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCountry(@PathVariable String id) {
        return countriesService.deleteCountry(id);
    }

    /**
     * Marca un país como eliminado lógicamente.
     * <p>
     * En lugar de eliminar el registro físicamente, se actualiza para indicar que está eliminado.
     * </p>
     *
     * @param id el identificador del país a marcar como eliminado.
     * @return un {@link Mono} que indica la finalización de la operación.
     */
    @PostMapping("/{id}/logicDelete")
    public Mono<Void> logicDeleteCountry(@PathVariable String id) {
        return countriesService.logicDeleteCountry(id);
    }

    /**
     * Restaura un país que fue marcado como eliminado lógicamente.
     *
     * @param id el identificador del país a restaurar.
     * @return un {@link Mono} que indica la finalización de la operación.
     */
    @PostMapping("/{id}/restore")
    public Mono<Void> restoreCountry(@PathVariable String id) {
        return countriesService.restoreCountry(id);
    }
}

