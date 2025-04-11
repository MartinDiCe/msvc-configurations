package com.diceprojects.msvcconfigurations.traces;

import com.diceprojects.msvcconfigurations.persistences.models.entities.ApiTrace;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * Bus de eventos para la traza de la API.
 * Los ApiTrace publicados serán escritos en la base de datos de forma asíncrona.
 */
public class ApiTraceBus {

    private static final Sinks.Many<ApiTrace> sink = Sinks.many().replay().limit(100);

    /**
     * Publica una traza en el bus.
     *
     * @param trace La traza a publicar.
     */
    public static void publish(ApiTrace trace) {
        sink.tryEmitNext(trace);
    }

    /**
     * Retorna un Flux para suscribirse a las trazas emitidas.
     *
     * @return un {@link Flux} de ApiTrace.
     */
    public static Flux<ApiTrace> asFlux() {
        return sink.asFlux();
    }
}