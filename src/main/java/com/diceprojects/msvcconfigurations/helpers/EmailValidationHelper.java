package com.diceprojects.msvcconfigurations.helpers;

import com.diceprojects.msvcconfigurations.persistences.models.entities.Parameter;
import com.diceprojects.msvcconfigurations.services.parameterServices.ParameterService;
import jakarta.annotation.PostConstruct;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Helper para la validación de direcciones de correo electrónico combinando
 * Apache Commons Validator y validaciones personalizadas.
 * <p>
 * Primero se valida el formato del email utilizando Apache Commons Validator,
 * y luego se verifica que el dominio pertenezca al conjunto de dominios permitidos,
 * que se carga dinámicamente desde la base de datos a través del parámetro "EmailDomains".
 * </p>
 */
@Component
public class EmailValidationHelper {

    /**
     * Expresión regular para detectar espacios en blanco.
     */
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

    /**
     * Servicio para obtener parámetros.
     */
    private final ParameterService parameterService;

    /**
     * Lista de dominios permitidos, cargada dinámicamente.
     */
    private List<String> allowedDomains;

    public EmailValidationHelper(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    /**
     * Método de inicialización que se ejecuta después de construir el bean.
     * Se encarga de cargar la lista de dominios permitidos desde el parámetro "EmailDomains".
     */
    @PostConstruct
    public void init() {
        // Se carga el parámetro "EmailDomains" desde la base de datos.
        parameterService.getParameterByName("EmailDomains")
                .map(Parameter::getParameterValue)
                .doOnNext(value -> {
                    // Separamos los dominios por comas y los convertimos a minúsculas
                    allowedDomains = Arrays.asList(value.split("\\s*,\\s*"));
                })
                .doOnError(e -> {
                    // Si ocurre algún error, se asigna una lista por defecto
                    allowedDomains = List.of("gmail.com", "hotmail.com", "outlook.com", "yahoo.com");
                })
                .subscribe();
    }

    /**
     * Valida si la dirección de correo electrónico proporcionada es válida.
     * <p>
     * Se utiliza Apache Commons Validator para comprobar el formato básico y, adicionalmente,
     * se verifica que el dominio se encuentre en el conjunto de dominios permitidos (cargado dinámicamente).
     * </p>
     *
     * @param email la dirección de correo electrónico a validar.
     * @return {@code true} si el email cumple con el formato esperado y el dominio es permitido;
     *         {@code false} en caso contrario.
     */
    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        // Recorta el email
        String trimmedEmail = email.trim();
        // Verifica que no contenga espacios internos
        if (WHITESPACE_PATTERN.matcher(trimmedEmail).find()) {
            return false;
        }
        // Valida el formato básico
        if (!EmailValidator.getInstance().isValid(trimmedEmail)) {
            return false;
        }
        // Extrae el dominio
        int atIndex = trimmedEmail.lastIndexOf('@');
        if (atIndex == -1 || atIndex == trimmedEmail.length() - 1) {
            return false;
        }
        String domain = trimmedEmail.substring(atIndex + 1).toLowerCase();

        if (allowedDomains == null || allowedDomains.isEmpty()) {
            allowedDomains = List.of("gmail.com", "hotmail.com", "outlook.com", "yahoo.com");
        }
        return allowedDomains.contains(domain);
    }
}
