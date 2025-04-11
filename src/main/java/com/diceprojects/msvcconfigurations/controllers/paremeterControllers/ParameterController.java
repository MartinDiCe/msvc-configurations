package com.diceprojects.msvcconfigurations.controllers.paremeterControllers;

import com.diceprojects.msvcconfigurations.persistences.models.dtos.parameters.ParameterMapper;
import com.diceprojects.msvcconfigurations.persistences.models.dtos.parameters.ParameterRequest;
import com.diceprojects.msvcconfigurations.persistences.models.dtos.parameters.ParameterResponse;
import com.diceprojects.msvcconfigurations.persistences.models.entities.Parameter;
import com.diceprojects.msvcconfigurations.services.parameterServices.ParameterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador reactivo para la entidad {@link Parameter}.
 * <p>
 * Expone endpoints para la creación, actualización parcial, eliminación (física o lógica)
 * y consulta de parámetros globales.
 * </p>
 *
 * <p>
 * Este controlador forma parte de un servicio 100% reactivo, usando Spring WebFlux y R2DBC,
 * lo que garantiza operaciones no bloqueantes y escalabilidad ante altos volúmenes de solicitudes.
 * Además, la documentación de la API se genera automáticamente con SpringDoc OpenAPI y
 * el logging se gestiona mediante el middleware y el AppLogger.
 * </p>
 */
@RestController
@RequestMapping("/api/parameters")
public class ParameterController {

    private final ParameterService parameterService;

    public ParameterController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    /**
     * Obtiene un parámetro a partir de su identificador.
     *
     * @param id El identificador del parámetro.
     * @return Un {@link Mono} que emite la respuesta del parámetro si se encuentra.
     */
    @GetMapping("/{id}")
    public Mono<ParameterResponse> getParameterById(@PathVariable String id) {
        return parameterService.getParameterById(id)
                .map(ParameterMapper::toResponse);
    }

    /**
     * Busca un parámetro por su nombre (sin distinción de mayúsculas/minúsculas).
     *
     * @param parameterName El nombre del parámetro a buscar.
     * @return Un {@link Mono} que emite la respuesta del parámetro si se encuentra.
     */
    @GetMapping("/byName")
    public Mono<ParameterResponse> getParameterByName(@RequestParam String parameterName) {
        return parameterService.getParameterByName(parameterName)
                .map(ParameterMapper::toResponse);
    }

    /**
     * Obtiene todos los parámetros.
     *
     * @return Un {@link Flux} que emite las respuestas de todos los parámetros.
     */
    @GetMapping
    public Flux<ParameterResponse> getAllParameters() {
        return parameterService.getAllParameters()
                .map(ParameterMapper::toResponse);
    }

    /**
     * Crea un parámetro nuevo.
     * <p>
     * Se espera que en la solicitud no se incluya un identificador, ya que este se genera automáticamente.
     * </p>
     *
     * @param request El DTO de solicitud con los datos del parámetro.
     * @return Un {@link Mono} que emite la respuesta del parámetro creado, con código de estado 201 (CREATED).
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ParameterResponse> createParameter(@Valid @RequestBody ParameterRequest request) {
        Parameter parameter = ParameterMapper.toEntity(request);
        return parameterService.createParameter(parameter)
                .map(ParameterMapper::toResponse);
    }

    /**
     * Actualiza de forma parcial un parámetro.
     * <p>
     * El endpoint espera el id en el DTO para identificar el registro a actualizar
     * y solamente actualiza aquellos campos que vienen con valores no nulos y no vacíos.
     * Si no se detectan cambios, se retorna el mismo parámetro sin modificarse.
     * </p>
     *
     * @param request El DTO de solicitud con el id y los campos a actualizar.
     * @return Un {@link Mono} que emite la respuesta del parámetro actualizado.
     */
    @PutMapping
    public Mono<ParameterResponse> updateParameter(@Valid @RequestBody ParameterRequest request) {
        Parameter parameter = ParameterMapper.toEntity(request);
        return parameterService.updateParameter(parameter)
                .map(ParameterMapper::toResponse);
    }

    /**
     * Elimina físicamente un parámetro.
     *
     * @param id El identificador del parámetro a eliminar.
     * @return Un {@link Mono} que indica la finalización de la operación con código 204 (NO_CONTENT).
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteParameter(@PathVariable String id) {
        return parameterService.deleteParameter(id);
    }

    /**
     * Marca un parámetro como eliminado de forma lógica.
     *
     * @param id El identificador del parámetro a marcar como eliminado.
     * @return Un {@link Mono} que indica la finalización de la operación.
     */
    @PostMapping("/{id}/logicDelete")
    public Mono<Void> logicDeleteParameter(@PathVariable String id) {
        return parameterService.logicDeleteParameter(id);
    }

    /**
     * Restaura un parámetro que fue previamente marcado como eliminado lógicamente.
     *
     * @param id El identificador del parámetro a restaurar.
     * @return Un {@link Mono} que indica la finalización de la operación.
     */
    @PostMapping("/{id}/restore")
    public Mono<Void> restoreParameter(@PathVariable String id) {
        return parameterService.restoreParameter(id);
    }
}

