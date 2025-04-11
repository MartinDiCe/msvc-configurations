package com.diceprojects.msvcconfigurations.traces;

import com.diceprojects.msvcconfigurations.persistences.models.entities.ApiTrace;
import com.diceprojects.msvcconfigurations.services.apiTraceServices.ApiTraceService;
import com.diceprojects.msvcconfigurations.logging.AppLogger;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * Componente que se suscribe al bus de trazas (ApiTraceBus) para procesar y persistir
 * de forma asíncrona cada {@link ApiTrace} emitida.
 * <p>
 * Este componente se inicia durante el arranque de la aplicación gracias a la anotación
 * {@code @PostConstruct}. Se encarga de escuchar el flujo de trazas publicado en el
 * {@link ApiTraceBus} y, por cada traza recibida, utiliza {@link ApiTraceService} para
 * persistirla en la base de datos.
 * </p>
 * <p>
 * Además, todos los eventos (éxito o error) se registran usando {@link AppLogger} para
 * mantener la trazabilidad y un registro de las operaciones en función del perfil (por ejemplo, dev o prod).
 * </p>
 */
@Component
public class ApiTraceSubscriber {

    private final ApiTraceService apiTraceService;
    private final AppLogger appLogger;

    /**
     * Constructor para inyectar el servicio de trazas y el logger personalizado.
     *
     * @param apiTraceService el servicio encargado de gestionar (persistir) las trazas de la API.
     * @param appLogger       la implementación de {@link AppLogger} para registrar eventos.
     */
    public ApiTraceSubscriber(ApiTraceService apiTraceService, AppLogger appLogger) {
        this.apiTraceService = apiTraceService;
        this.appLogger = appLogger;
    }

    /**
     * Inicia la suscripción al bus de trazas para procesar cada {@link ApiTrace} emitida.
     * <p>
     * Cada vez que se recibe una traza desde el {@link ApiTraceBus}, se invoca el método
     * {@code createAsync} de {@link ApiTraceService} para persistirla de forma asíncrona.
     * Los resultados se registran: si la traza se guarda correctamente, se indica su ID; si ocurre algún error,
     * se registra con el nivel de error correspondiente.
     * </p>
     */
    @PostConstruct
    public void startTraceSubscription() {
        Flux<ApiTrace> traceFlux = ApiTraceBus.asFlux();
        traceFlux.subscribe(
                trace -> {
                    apiTraceService.createAsync(trace)
                            .subscribe(
                                    saved -> appLogger.info("ApiTrace guardado con id: {}", saved.getApiTraceId()),
                                    error -> appLogger.error("Error al guardar ApiTrace", error)
                            );
                },
                error -> appLogger.error("Error en la suscripción del bus de trazas", error)
        );
    }
}
