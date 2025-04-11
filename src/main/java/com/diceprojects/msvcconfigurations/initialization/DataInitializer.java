package com.diceprojects.msvcconfigurations.initialization;

import com.diceprojects.msvcconfigurations.exceptions.ErrorHandler;
import com.diceprojects.msvcconfigurations.logging.AppLogger;
import com.diceprojects.msvcconfigurations.persistences.models.entities.Parameter;
import com.diceprojects.msvcconfigurations.persistences.models.enums.ParameterCategory;
import com.diceprojects.msvcconfigurations.services.parameterServices.ParameterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Inicializador de datos para crear parámetros predeterminados durante el arranque de la aplicación.
 * <p>
 * Esta clase implementa {@link CommandLineRunner} para ejecutar código específico al iniciar la aplicación.
 * Se asegura de que ciertos parámetros críticos existan en la base de datos.
 * En este caso, se crean o validan los parámetros "TokenTimeOut", "ContactType", "EntityType", "PhoneType" y "EmailDomains".
 * </p>
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final ParameterService parameterService;
    private final AppLogger appLogger;

    /**
     * Constructor que inyecta el servicio de parámetros y la abstracción de logging.
     *
     * @param parameterService el servicio para gestionar los parámetros del sistema.
     * @param appLogger        la implementación de {@link AppLogger} para registrar logs.
     */
    public DataInitializer(ParameterService parameterService, AppLogger appLogger) {
        this.parameterService = parameterService;
        this.appLogger = appLogger;
    }

    /**
     * Método que se ejecuta al inicio de la aplicación.
     * <p>
     * Se invocan los métodos que verifican y crean los parámetros "TokenTimeOut", "ContactType", "EntityType",
     * "PhoneType" y "EmailDomains" en caso de no existir.
     * </p>
     *
     * @param args argumentos de la línea de comandos.
     */
    @Override
    public void run(String... args) {
        createTokenTimeOutParameterIfNotFound()
                .then(createContactTypeParameterIfNotFound())
                .then(createEntityTypeParameterIfNotFound())
                .then(createPhoneTypeParameterIfNotFound())
                .then(createEmailDomainsParameterIfNotFound())
                .then(createApiTraceActiveParameterIfNotFound())
                .then(createJacksonTimeZoneParameterIfNotFound())
                .then(createJacksonDateFormatParameterIfNotFound())
                .subscribe(
                        result -> appLogger.info("Inicialización completada con éxito"),
                        error -> ErrorHandler.handleError("Error al inicializar los datos", error, HttpStatus.INTERNAL_SERVER_ERROR)
                );
    }

    private Mono<Parameter> createTokenTimeOutParameterIfNotFound() {
        return parameterService.getParameterByName("TokenTimeOut")
                .switchIfEmpty(Mono.defer(() -> {
                    Parameter parameter = new Parameter();
                    parameter.setParameterName("TokenTimeOut");
                    parameter.setParameterValue("{\"timeout\":\"1800\"}");
                    parameter.setParameterDescription("Duración del tiempo de espera del token en segundos");
                    parameter.setParameterCategory(ParameterCategory.TIMEOUT);
                    return parameterService.saveOrUpdateParameter(parameter)
                            .doOnNext(savedParameter -> appLogger.info("Parámetro TokenTimeOut creado con éxito: {}", savedParameter.getParameterName()))
                            .doOnError(e -> ErrorHandler.handleError("Error al crear el parámetro TokenTimeOut", e, HttpStatus.INTERNAL_SERVER_ERROR));
                }))
                .doOnNext(parameter -> appLogger.info("El parámetro TokenTimeOut ya existe: {}", parameter.getParameterName()))
                .doOnError(e -> ErrorHandler.handleError("Error al buscar el parámetro TokenTimeOut", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Mono<Parameter> createContactTypeParameterIfNotFound() {
        return parameterService.getParameterByName("ContactType")
                .switchIfEmpty(Mono.defer(() -> {
                    Parameter parameter = new Parameter();
                    parameter.setParameterName("ContactType");
                    parameter.setParameterValue("EMAIL,TELEFONO,DOMICILIO");
                    parameter.setParameterDescription("Valores posibles para el campo ContactType");
                    parameter.setParameterCategory(ParameterCategory.SYSTEM);
                    return parameterService.saveOrUpdateParameter(parameter)
                            .doOnNext(savedParameter -> appLogger.info("Parámetro ContactType creado con éxito: {}", savedParameter.getParameterName()))
                            .doOnError(e -> ErrorHandler.handleError("Error al crear el parámetro ContactType", e, HttpStatus.INTERNAL_SERVER_ERROR));
                }))
                .doOnNext(parameter -> appLogger.info("El parámetro ContactType ya existe: {}", parameter.getParameterName()))
                .doOnError(e -> ErrorHandler.handleError("Error al buscar el parámetro ContactType", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Mono<Parameter> createEntityTypeParameterIfNotFound() {
        return parameterService.getParameterByName("EntityType")
                .switchIfEmpty(Mono.defer(() -> {
                    Parameter parameter = new Parameter();
                    parameter.setParameterName("EntityType");
                    parameter.setParameterValue("COMPANY,PROVIDER,CUSTOMER,SELLER,STAFF,DISTRIBUTOR,RESELLER");
                    parameter.setParameterDescription("Valores posibles para el campo EntityType");
                    parameter.setParameterCategory(ParameterCategory.SYSTEM);
                    return parameterService.saveOrUpdateParameter(parameter)
                            .doOnNext(savedParameter -> appLogger.info("Parámetro EntityType creado con éxito: {}", savedParameter.getParameterName()))
                            .doOnError(e -> ErrorHandler.handleError("Error al crear el parámetro EntityType", e, HttpStatus.INTERNAL_SERVER_ERROR));
                }))
                .doOnNext(parameter -> appLogger.info("El parámetro EntityType ya existe: {}", parameter.getParameterName()))
                .doOnError(e -> ErrorHandler.handleError("Error al buscar el parámetro EntityType", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Mono<Parameter> createPhoneTypeParameterIfNotFound() {
        return parameterService.getParameterByName("PhoneType")
                .switchIfEmpty(Mono.defer(() -> {
                    Parameter parameter = new Parameter();
                    parameter.setParameterName("PhoneType");
                    parameter.setParameterValue("WHATSAPP,TRABAJO,CELULAR");
                    parameter.setParameterDescription("Valores posibles para el campo PhoneType");
                    parameter.setParameterCategory(ParameterCategory.SYSTEM);
                    return parameterService.saveOrUpdateParameter(parameter)
                            .doOnNext(savedParameter -> appLogger.info("Parámetro PhoneType creado con éxito: {}", savedParameter.getParameterName()))
                            .doOnError(e -> ErrorHandler.handleError("Error al crear el parámetro PhoneType", e, HttpStatus.INTERNAL_SERVER_ERROR));
                }))
                .doOnNext(parameter -> appLogger.info("El parámetro PhoneType ya existe: {}", parameter.getParameterName()))
                .doOnError(e -> ErrorHandler.handleError("Error al buscar el parámetro PhoneType", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Mono<Parameter> createEmailDomainsParameterIfNotFound() {
        return parameterService.getParameterByName("EmailDomains")
                .switchIfEmpty(Mono.defer(() -> {
                    Parameter parameter = new Parameter();
                    parameter.setParameterName("EmailDomains");
                    parameter.setParameterValue("gmail.com,hotmail.com,outlook.com,yahoo.com,live.com,aol.com,msn.com");
                    parameter.setParameterDescription("Valores posibles para los dominios de correo permitidos");
                    parameter.setParameterCategory(ParameterCategory.SYSTEM);
                    return parameterService.saveOrUpdateParameter(parameter)
                            .doOnNext(savedParameter -> appLogger.info("Parámetro EmailDomains creado con éxito: {}", savedParameter.getParameterName()))
                            .doOnError(e -> ErrorHandler.handleError("Error al crear el parámetro EmailDomains", e, HttpStatus.INTERNAL_SERVER_ERROR));
                }))
                .doOnNext(parameter -> appLogger.info("El parámetro EmailDomains ya existe: {}", parameter.getParameterName()))
                .doOnError(e -> ErrorHandler.handleError("Error al buscar el parámetro EmailDomains", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Mono<Parameter> createApiTraceActiveParameterIfNotFound() {
        return parameterService.getParameterByName("ApiTraceActive")
                .switchIfEmpty(Mono.defer(() -> {
                    Parameter parameter = new Parameter();
                    parameter.setParameterName("ApiTraceActive");
                    parameter.setParameterValue("true");
                    parameter.setParameterDescription("Activa o desactiva la captura de trazas de la API (true para activar, false para desactivar).");
                    parameter.setParameterCategory(ParameterCategory.SYSTEM);
                    return parameterService.saveOrUpdateParameter(parameter)
                            .doOnNext(savedParameter -> appLogger.info("Parámetro ApiTraceActive creado con éxito: {}", savedParameter.getParameterName()))
                            .doOnError(e -> ErrorHandler.handleError("Error al crear el parámetro ApiTraceActive", e, HttpStatus.INTERNAL_SERVER_ERROR));
                }))
                .doOnNext(parameter -> appLogger.info("El parámetro ApiTraceActive ya existe: {}", parameter.getParameterName()))
                .doOnError(e -> ErrorHandler.handleError("Error al buscar el parámetro ApiTraceActive", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Mono<Parameter> createJacksonDateFormatParameterIfNotFound() {
        return parameterService.getParameterByName("GlobalDateFormat")
                .switchIfEmpty(Mono.defer(() -> {
                    Parameter parameter = new Parameter();
                    parameter.setParameterName("GlobalDateFormat");
                    parameter.setParameterValue("HH:mm:ss");
                    parameter.setParameterDescription("Formato de fecha Global (por ejemplo, HH:mm:ss)");
                    parameter.setParameterCategory(ParameterCategory.SYSTEM);
                    return parameterService.saveOrUpdateParameter(parameter)
                            .doOnNext(savedParameter -> appLogger.info("Parámetro GlobalDateFormat creado con éxito: {}", savedParameter.getParameterName()))
                            .doOnError(e -> ErrorHandler.handleError("Error al crear el parámetro GlobalDateFormat", e, HttpStatus.INTERNAL_SERVER_ERROR));
                }))
                .doOnNext(parameter -> appLogger.info("El parámetro GlobalDateFormat ya existe: {}", parameter.getParameterName()))
                .doOnError(e -> ErrorHandler.handleError("Error al buscar el parámetro GlobalDateFormat", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Mono<Parameter> createJacksonTimeZoneParameterIfNotFound() {
        return parameterService.getParameterByName("GlobalTimeZone")
                .switchIfEmpty(Mono.defer(() -> {
                    Parameter parameter = new Parameter();
                    parameter.setParameterName("GlobalTimeZone");
                    parameter.setParameterValue("UTC");
                    parameter.setParameterDescription("Zona horaria Global de la Aplicación");
                    parameter.setParameterCategory(ParameterCategory.SYSTEM);
                    return parameterService.saveOrUpdateParameter(parameter)
                            .doOnNext(savedParameter -> appLogger.info("Parámetro GlobalTimeZone creado con éxito: {}", savedParameter.getParameterName()))
                            .doOnError(e -> ErrorHandler.handleError("Error al crear el parámetro GlobalTimeZone", e, HttpStatus.INTERNAL_SERVER_ERROR));
                }))
                .doOnNext(parameter -> appLogger.info("El parámetro GlobalTimeZone ya existe: {}", parameter.getParameterName()))
                .doOnError(e -> ErrorHandler.handleError("Error al buscar el parámetro GlobalTimeZone", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
