package com.diceprojects.msvcconfigurations.initialization;

import com.diceprojects.msvcconfigurations.exceptions.ErrorHandler;
import com.diceprojects.msvcconfigurations.persistences.models.entities.Parameter;
import com.diceprojects.msvcconfigurations.services.ParameterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Inicializador de datos para crear parámetros predeterminados durante el arranque de la aplicación.
 * <p>
 * Esta clase implementa {@link CommandLineRunner} para ejecutar código específico cuando la aplicación se inicia.
 * Se asegura de que ciertos parámetros, como el estado de la entidad ("EntityStatus"), existan en la base de datos.
 * Si el parámetro no existe, se crea automáticamente.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final ParameterService parameterService;

    /**
     * Constructor que inyecta el servicio de parámetros.
     *
     * @param parameterService el servicio utilizado para gestionar los parámetros de la aplicación.
     */
    public DataInitializer(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    /**
     * Método que se ejecuta al inicio de la aplicación.
     * <p>
     * Llama al método {@link #createEntityStatusParameterIfNotFound()} para crear el parámetro
     * "EntityStatus" si no existe.
     *
     * @param args los argumentos de la línea de comandos.
     */
    @Override
    public void run(String... args) {
        createEntityStatusParameterIfNotFound()
                .subscribe(
                        result -> System.out.println("Inicialización completada con éxito"),
                        error -> ErrorHandler.handleError("Error al inicializar los datos", error, HttpStatus.INTERNAL_SERVER_ERROR)
                );
    }

    /**
     * Crea el parámetro "EntityStatus" si no se encuentra en la base de datos.
     * <p>
     * Este método busca un parámetro con el nombre "EntityStatus". Si no se encuentra, crea uno nuevo
     * con un valor predeterminado que representa los estados "Activo" e "Inactivo" para las entidades.
     *
     * @return un {@link Mono} que emite el parámetro creado o encontrado, o un error si la creación falla.
     */
    private Mono<Parameter> createEntityStatusParameterIfNotFound() {
        return parameterService.getParameterByName("EntityStatus")
                .switchIfEmpty(Mono.defer(() -> {
                    Parameter parameter = new Parameter();
                    parameter.setParameterName("EntityStatus");
                    parameter.setValue("{\"status1\":\"Active\", \"status2\":\"Inactive\"}");
                    parameter.setDescription("Parámetro de estado de entidad para gestionar los estados activo/inactivo");

                    return parameterService.saveOrUpdateParameter(parameter)
                            .doOnNext(savedParameter -> System.out.println("Parámetro EntityStatus creado con éxito: " + savedParameter.getParameterName()))
                            .doOnError(e -> ErrorHandler.handleError("Error al crear el parámetro EntityStatus", e, HttpStatus.INTERNAL_SERVER_ERROR));
                }))
                .doOnNext(parameter -> System.out.println("El parámetro EntityStatus ya existe: " + parameter.getParameterName()))
                .doOnError(e -> ErrorHandler.handleError("Error al buscar el parámetro EntityStatus", e, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
