package com.diceprojects.msvcconfigurations.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;

/**
 * Configuración para personalizar la estrategia de nombres de columnas en Spring Data R2DBC.
 * <p>
 * Por defecto, Spring convierte camelCase → snake_case (por ejemplo, parameterName → parameter_name).
 * Con esta configuración mantenemos los nombres tal cual (por ejemplo, parameterName).
 * </p>
 */
@Configuration
public class R2dbcNamingConfiguration {

    /**
     * Crea un R2dbcMappingContext que utiliza un NamingStrategy personalizado que
     * devuelve el nombre de la propiedad tal como se define (sin convertirlo).
     *
     * @return R2dbcMappingContext configurado con la estrategia identidad para nombres de columna.
     */
    @Bean
    @Primary
    public R2dbcMappingContext r2dbcMappingContext() {
        NamingStrategy namingStrategy = new NamingStrategy() {
            @Override
            public String getColumnName(RelationalPersistentProperty property) {
                return property.getName();
            }
            @Override
            public String getTableName(Class<?> type) {
                return type.getSimpleName();
            }
        };
        return new R2dbcMappingContext(namingStrategy);
    }

    @Bean(name = "customR2dbcConverter")
    @Primary
    public R2dbcConverter customR2dbcConverter(R2dbcMappingContext mappingContext,
                                               R2dbcCustomConversions conversions) {
        return new MappingR2dbcConverter(mappingContext, conversions);
    }
}
