package com.diceprojects.msvcconfigurations.configurations;

import com.diceprojects.msvcconfigurations.persistences.models.entities.Parameter;
import com.diceprojects.msvcconfigurations.services.parameterServices.ParameterService;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    private final ParameterService parameterService;

    public JacksonConfig(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    /**
     * Configura Jackson para usar el formato de fecha y la zona horaria definidos en los parámetros "GlobalDateFormat" y "GlobalTimeZone".
     * Se consulta el ParameterService de forma reactiva y se bloquea para obtener los valores durante el arranque.
     *
     * @return un Jackson2ObjectMapperBuilderCustomizer que configura el ObjectMapper.
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        // Consultar los parámetros de forma reactiva y bloquear para obtenerlos sincrónicamente.
        String dateFormat = parameterService.getParameterByName("GlobalDateFormat")
                .map(Parameter::getParameterValue)
                .defaultIfEmpty("HH:mm:ss")
                .block();

        String timeZone = parameterService.getParameterByName("GlobalTimeZone")
                .map(Parameter::getParameterValue)
                .defaultIfEmpty("UTC")
                .block();

        if (dateFormat == null || dateFormat.isEmpty()) {
            dateFormat = "HH:mm:ss";
        }
        if (timeZone == null || timeZone.isEmpty()) {
            timeZone = "UTC";
        }

        String finalDateFormat = dateFormat;
        String finalTimeZone = timeZone;
        return builder -> {
            builder.simpleDateFormat(finalDateFormat);
            builder.timeZone(TimeZone.getTimeZone(finalTimeZone));
        };
    }
}
