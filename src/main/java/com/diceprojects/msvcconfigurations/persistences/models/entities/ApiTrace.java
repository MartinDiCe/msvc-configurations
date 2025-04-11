package com.diceprojects.msvcconfigurations.persistences.models.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad que registra la traza de una solicitud/respuesta de la API.
 */
@Data
@Table("apiTraces")
public class ApiTrace {

    /**
     * Identificador único del registro.
     * Se asume auto-incrementable definido en la base de datos (INT).
     */
    @Id
    private Integer apiTraceId;

    /** Nombre o endpoint del servicio invocado. */
    private String serviceName = "";

    /** IP o información del origen de la solicitud. */
    private String requestOrigin = "";

    /** Fecha y hora en que se realizó la solicitud. */
    private LocalDateTime requestTimestamp;

    /** Método HTTP de la solicitud (GET, POST, etc.). */
    private String httpMethod = "";

    /** Cuerpo de la solicitud. */
    private String requestPayload = "";

    /** Cuerpo de la respuesta. */
    private String responsePayload = "";

    /** Tiempo de ejecución de la solicitud en segundos. */
    private double executionTimeSeconds;

    /** Peso total de la transmisión (por ejemplo, en bytes). */
    private int payloadSize;

    /** Código HTTP de la respuesta. */
    private int httpResponseCode;

}
