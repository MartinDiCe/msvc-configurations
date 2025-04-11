package com.diceprojects.msvcconfigurations.configurations;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * Configuración de R2DBC que, en el arranque:
 * <ol>
 *   <li>Se conecta a master y crea la base de datos EGM si no existe.</li>
 *   <li>Se conecta a EGM y crea el esquema (tablas) si no existen.</li>
 *   <li>Devuelve un {@link ConnectionFactory} apuntando a EGM listo para usar.</li>
 * </ol>
 * Se utilizan nombres de columnas que incluyen el prefijo de la entidad para los datos específicos,
 * pero los campos de auditoría se definen de manera uniforme (createdDate, createdBy, modifiedDate, modifiedBy, deleted, deletedDate, deletedBy).
 */
@Configuration
public class R2dbcConfiguration {

    @Value("${spring.r2dbc.master.url}")
    private String masterUrl;

    @Value("${spring.r2dbc.egm.url}")
    private String egmUrl;

    @Value("${spring.r2dbc.username}")
    private String username;

    @Value("${spring.r2dbc.password}")
    private String password;

    /**
     * Bean único de ConnectionFactory:
     * 1) Crea la BD EGM en master si falta.
     * 2) Conecta a EGM y crea las tablas si faltan.
     * 3) Devuelve la CF ya apuntando a EGM.
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        // 1. Conectar a master
        ConnectionFactory masterCf = ConnectionFactories.get(
                ConnectionFactoryOptions.parse(masterUrl)
                        .mutate()
                        .option(ConnectionFactoryOptions.USER, username)
                        .option(ConnectionFactoryOptions.PASSWORD, password)
                        .build()
        );

        // 2. Crear BD EGM si no existe
        Mono.usingWhen(
                masterCf.create(),
                conn -> Mono.from(conn.createStatement(
                        "IF NOT EXISTS (SELECT 1 FROM sys.databases WHERE name = 'EGM') " +
                                "BEGIN CREATE DATABASE [EGM]; END"
                ).execute()).then(),
                Connection::close
        ).block();

        // 3. Conectar a EGM
        ConnectionFactory egmCf = ConnectionFactories.get(
                ConnectionFactoryOptions.parse(egmUrl)
                        .mutate()
                        .option(ConnectionFactoryOptions.USER, username)
                        .option(ConnectionFactoryOptions.PASSWORD, password)
                        .build()
        );

        Mono.usingWhen(
                egmCf.create(),
                conn -> {
                    // Tabla parameters
                    Mono<Void> p = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('parameters','U') IS NULL " +
                                    "CREATE TABLE parameters (" +
                                    "  parameterId VARCHAR(36) PRIMARY KEY, " +
                                    "  parameterName NVARCHAR(100) NOT NULL, " +
                                    "  parameterValue NVARCHAR(255) NOT NULL, " +
                                    "  parameterDescription NVARCHAR(500) NULL, " +
                                    "  parameterCategory NVARCHAR(50) NULL, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla countries
                    Mono<Void> c = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('countries','U') IS NULL " +
                                    "CREATE TABLE countries (" +
                                    "  countryId VARCHAR(36) PRIMARY KEY, " +
                                    "  countryName NVARCHAR(255) NOT NULL, " +
                                    "  countryCode NVARCHAR(50) NULL, " +
                                    "  countryCodePhone INT NOT NULL, " +
                                    "  countryDescription NVARCHAR(255) NULL, " +
                                    "  countryTimeZone NVARCHAR(255) NULL, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla tax_settings
                    Mono<Void> t = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('taxSettings','U') IS NULL " +
                                    "CREATE TABLE taxSettings (" +
                                    "  taxSettingId VARCHAR(36) PRIMARY KEY, " +
                                    "  taxSettingName NVARCHAR(255) NOT NULL, " +
                                    "  taxSettingRate FLOAT NOT NULL, " +
                                    "  taxSettingCountryId VARCHAR(36) NOT NULL, " +
                                    "  taxSettingStateId VARCHAR(36) NULL, " +
                                    "  taxSettingDescription NVARCHAR(255) NULL, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla states
                    Mono<Void> s = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('states','U') IS NULL " +
                                    "CREATE TABLE states (" +
                                    "  stateId VARCHAR(36) PRIMARY KEY, " +
                                    "  stateName NVARCHAR(255) NOT NULL, " +
                                    "  stateCode NVARCHAR(50) NULL, " +
                                    "  stateDescription NVARCHAR(255) NULL, " +
                                    "  stateCountryId VARCHAR(36) NOT NULL, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla shipping_methods
                    Mono<Void> sh = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('shippingMethods','U') IS NULL " +
                                    "CREATE TABLE shippingMethods (" +
                                    "  shippingMethodId VARCHAR(36) PRIMARY KEY, " +
                                    "  shippingMethodName NVARCHAR(255) NOT NULL, " +
                                    "  shippingMethodCost DECIMAL(10,2) NULL, " +
                                    "  shippingMethodEstimatedDeliveryDays INT NULL, " +
                                    "  shippingMethodDescription NVARCHAR(255) NULL, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla phone
                    Mono<Void> ph = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('phone','U') IS NULL " +
                                    "CREATE TABLE phone (" +
                                    "  phoneId VARCHAR(36) PRIMARY KEY, " +
                                    "  phoneNumber NVARCHAR(50) NOT NULL, " +
                                    "  phoneExtension NVARCHAR(20) NULL, " +
                                    "  phoneType NVARCHAR(50) NULL, " +
                                    "  phoneCountryId VARCHAR(36) NULL, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla payment_methods
                    Mono<Void> pm = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('paymentMethods','U') IS NULL " +
                                    "CREATE TABLE paymentMethods (" +
                                    "  paymentMethodId VARCHAR(36) PRIMARY KEY, " +
                                    "  paymentMethodName NVARCHAR(255) NOT NULL, " +
                                    "  paymentMethodDescription NVARCHAR(255) NULL, " +
                                    "  paymentMethodEnabled BIT NOT NULL DEFAULT 0, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla notification_types
                    Mono<Void> nt = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('notificationTypes','U') IS NULL " +
                                    "CREATE TABLE notificationTypes (" +
                                    "  notificationTypeId VARCHAR(36) PRIMARY KEY, " +
                                    "  notificationTypeName NVARCHAR(255) NOT NULL, " +
                                    "  notificationTypeDescription NVARCHAR(255) NULL, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla notification_templates
                    Mono<Void> ntemp = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('notificationTemplates','U') IS NULL " +
                                    "CREATE TABLE notificationTemplates (" +
                                    "  notificationTemplateId VARCHAR(36) PRIMARY KEY, " +
                                    "  notificationTemplateName NVARCHAR(255) NOT NULL, " +
                                    "  notificationTemplateSubject NVARCHAR(255) NOT NULL, " +
                                    "  notificationTemplateBody NVARCHAR(MAX) NOT NULL, " +
                                    "  notificationTemplateTypeId VARCHAR(36) NOT NULL, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla neighborhoods
                    Mono<Void> nh = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('neighborhoods','U') IS NULL " +
                                    "CREATE TABLE neighborhoods (" +
                                    "  neighborhoodId VARCHAR(36) PRIMARY KEY, " +
                                    "  neighborhoodName NVARCHAR(255) NOT NULL, " +
                                    "  neighborhoodLocalityId VARCHAR(36) NOT NULL, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla localities
                    Mono<Void> nl = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('localities','U') IS NULL " +
                                    "CREATE TABLE localities (" +
                                    "  localityId VARCHAR(36) PRIMARY KEY, " +
                                    "  localityName NVARCHAR(255) NOT NULL, " +
                                    "  localityStateId VARCHAR(36) NULL, " +
                                    "  localityPostalCode NVARCHAR(50) NULL, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla languages
                    Mono<Void> lang = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('languages','U') IS NULL " +
                                    "CREATE TABLE languages (" +
                                    "  languageId VARCHAR(36) PRIMARY KEY, " +
                                    "  languageName NVARCHAR(255) NOT NULL, " +
                                    "  languageIsoCode NVARCHAR(10) NOT NULL, " +
                                    "  languageDescription NVARCHAR(255) NULL, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla feature_toggles
                    Mono<Void> ft = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('featureToggles','U') IS NULL " +
                                    "CREATE TABLE featureToggles (" +
                                    "  featureToggleId VARCHAR(36) PRIMARY KEY, " +
                                    "  featureToggleName NVARCHAR(255) NOT NULL, " +
                                    "  featureToggleEnabled BIT NOT NULL DEFAULT 0, " +
                                    "  featureToggleDescription NVARCHAR(255) NULL, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla email
                    Mono<Void> em = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('email','U') IS NULL " +
                                    "CREATE TABLE email (" +
                                    "  emailId VARCHAR(36) PRIMARY KEY, " +
                                    "  emailAddress NVARCHAR(255) NOT NULL, " +
                                    "  emailVerified BIT NOT NULL DEFAULT 0, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla currencies
                    Mono<Void> cur = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('currencies','U') IS NULL " +
                                    "CREATE TABLE currencies (" +
                                    "  currencyId VARCHAR(36) PRIMARY KEY, " +
                                    "  currencyCode NVARCHAR(10) NOT NULL, " +
                                    "  currencyName NVARCHAR(255) NOT NULL, " +
                                    "  currencySymbol NVARCHAR(10) NULL, " +
                                    "  currencyConversionRate FLOAT NULL, " +
                                    "  currencyBase BIT NOT NULL DEFAULT 0, " +
                                    "  currencyDescription NVARCHAR(255) NULL, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla contact_detail
                    Mono<Void> cd = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('contactDetail','U') IS NULL " +
                                    "CREATE TABLE contactDetail (" +
                                    "  contactDetailId VARCHAR(36) PRIMARY KEY, " +
                                    "  contactType NVARCHAR(50) NULL, " +
                                    "  entityType NVARCHAR(50) NULL, " +
                                    "  entityId VARCHAR(36) NULL, " +
                                    "  detailId VARCHAR(36) NULL, " +
                                    "  principal BIT NOT NULL DEFAULT 0, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla cities
                    Mono<Void> ci = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('cities','U') IS NULL " +
                                    "CREATE TABLE cities (" +
                                    "  cityId VARCHAR(36) PRIMARY KEY, " +
                                    "  cityName NVARCHAR(255) NOT NULL, " +
                                    "  cityDescription NVARCHAR(255) NULL, " +
                                    "  cityStateId VARCHAR(36) NULL, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    // Tabla address
                    Mono<Void> ad = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('address','U') IS NULL " +
                                    "CREATE TABLE address (" +
                                    "  addressId VARCHAR(36) PRIMARY KEY, " +
                                    "  addressStreet NVARCHAR(255) NULL, " +
                                    "  addressCityId VARCHAR(36) NULL, " +
                                    "  addressPostalCode NVARCHAR(50) NULL, " +
                                    "  addressStateId VARCHAR(36) NULL, " +
                                    "  addressCountryId VARCHAR(36) NULL, " +
                                    "  addressLatitude FLOAT NULL, " +
                                    "  addressLongitude FLOAT NULL, " +
                                    "  addressNeighborhood NVARCHAR(255) NULL, " +
                                    "  addressFormatted NVARCHAR(255) NULL, " +
                                    "  addressNormalization NVARCHAR(MAX) NULL, " +
                                    "  addressCrossStreets NVARCHAR(255) NULL, " +
                                    "  addressObservations NVARCHAR(255) NULL, " +
                                    "  createdDate DATETIME2 NOT NULL DEFAULT GETDATE(), " +
                                    "  createdBy NVARCHAR(100) NULL, " +
                                    "  modifiedDate DATETIME2 NULL, " +
                                    "  modifiedBy NVARCHAR(100) NULL, " +
                                    "  deleted BIT NOT NULL DEFAULT 0, " +
                                    "  deletedDate DATETIME2 NULL, " +
                                    "  deletedBy NVARCHAR(100) NULL" +
                                    ")"
                    ).execute()).then();

                    Mono<Void> att = Mono.from(conn.createStatement(
                            "IF OBJECT_ID('apiTraces','U') IS NULL " +
                                    "CREATE TABLE apiTraces (" +
                                    "  apiTraceId INT IDENTITY(1,1) PRIMARY KEY, " +
                                    "  serviceName NVARCHAR(100) NOT NULL, " +
                                    "  requestOrigin NVARCHAR(50) NOT NULL, " +
                                    "  requestTimestamp DATETIME2 NOT NULL, " +
                                    "  httpMethod NVARCHAR(10) NOT NULL, " +
                                    "  requestPayload NVARCHAR(MAX) NULL, " +
                                    "  responsePayload NVARCHAR(MAX) NULL, " +
                                    "  executionTimeSeconds FLOAT NOT NULL, " +
                                    "  payloadSize INT NOT NULL, " +
                                    "  httpResponseCode INT NOT NULL" +
                                    ")"
                    ).execute()).then();

                    return p.then(c)
                            .then(t)
                            .then(s)
                            .then(sh)
                            .then(ph)
                            .then(pm)
                            .then(nt)
                            .then(ntemp)
                            .then(nh)
                            .then(nl)
                            .then(lang)
                            .then(ft)
                            .then(em)
                            .then(cur)
                            .then(cd)
                            .then(ci)
                            .then(ad)
                            .then(att);
                },
                Connection::close
        ).block();

        return egmCf;
    }
}
