package com.diceprojects.msvcconfigurations.controllers;

import com.diceprojects.msvcconfigurations.persistences.models.entities.Parameter;
import com.diceprojects.msvcconfigurations.services.ParameterService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador para manejar las solicitudes relacionadas con los parámetros del sistema.
 */
@RestController
@RequestMapping("/api/parameters")
public class ParameterController {

    private final ParameterService parameterService;

    public ParameterController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    /**
     * Endpoint para obtener un parámetro por su nombre.
     *
     * @param parameterName el nombre del parámetro
     * @return un Mono que emite el parámetro encontrado
     */
    @GetMapping("/getParameterName/{parameterName}")
    public Mono<Parameter> getParameterByName(@PathVariable String parameterName) {
        return parameterService.getParameterByName(parameterName);
    }

    /**
     * Endpoint para crear o actualizar un parámetro.
     *
     * @param parameter el objeto Parameter a crear o actualizar
     * @return un Mono que emite el parámetro guardado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Parameter> saveOrUpdateParameter(@RequestBody Parameter parameter) {
        return parameterService.saveOrUpdateParameter(parameter);
    }

    /**
     * Endpoint para eliminar un parámetro por su ID.
     *
     * @param parameterId el ID del parámetro a eliminar
     * @return un Mono vacío que indica la finalización de la eliminación
     */
    @DeleteMapping("/delete/{parameterId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteParameterById(@PathVariable String parameterId) {
        return parameterService.deleteParameterById(parameterId);
    }

    /**
     * Endpoint para obtener todos los parámetros.
     *
     * @return un Flux que emite todos los parámetros
     */
    @GetMapping("/ListAll")
    public Flux<Parameter> getAllParameters() {
        return parameterService.getAllParameters();
    }
}
