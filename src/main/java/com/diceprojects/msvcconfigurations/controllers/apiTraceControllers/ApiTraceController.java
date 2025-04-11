package com.diceprojects.msvcconfigurations.controllers.apiTraceControllers;

import com.diceprojects.msvcconfigurations.persistences.models.entities.ApiTrace;
import com.diceprojects.msvcconfigurations.services.apiTraceServices.ApiTraceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Controlador para consultar las trazas de la API.
 * <p>
 * Este controlador expone un endpoint para obtener todas las trazas registradas,
 * transformando la entidad ApiTrace en un DTO ApiTraceResponse para mejorar la presentación
 * (por ejemplo, formateando correctamente la fecha).
 * </p>
 */
@RestController
@RequestMapping("/api/apitraces")
public class ApiTraceController {

    private final ApiTraceService apiTraceService;

    public ApiTraceController(ApiTraceService apiTraceService) {
        this.apiTraceService = apiTraceService;
    }

    /**
     * Obtiene todas las trazas registradas.
     *
     * @return un {@link Flux} de {@link ApiTrace} con todas las trazas.
     */
    @GetMapping
    public Flux<ApiTrace> getAllTraces() {
        return apiTraceService.getAllTraces();
    }
}
