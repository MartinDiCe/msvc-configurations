package com.diceprojects.msvcconfigurations.clients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Clase de configuración para crear y configurar instancias de {@link WebClient.Builder}.
 *
 * <p>Esta clase define un bean de {@link WebClient.Builder} que puede ser utilizado en toda la aplicación para
 * realizar solicitudes HTTP de manera reactiva.</p>
 */
@Configuration
public class WebClientConfig {

    /**
     * Define un bean de {@link WebClient.Builder}.
     *
     * <p>Este método crea y devuelve un {@link WebClient.Builder} configurado que puede ser inyectado en otros
     * componentes de la aplicación donde se necesite realizar solicitudes HTTP de manera reactiva.</p>
     *
     * @return una instancia de {@link WebClient.Builder}.
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
