USE [EGM];
GO

/******************************************************
 * TABLA: address
 ******************************************************/
CREATE TABLE address (
                         id                 VARCHAR(36)      NOT NULL PRIMARY KEY,
                         street             NVARCHAR(255)    NULL,
                         cityId             VARCHAR(36)      NULL,
                         postalCode         NVARCHAR(50)     NULL,
                         stateId            VARCHAR(36)      NULL,
                         countryId          VARCHAR(36)      NULL,
                         latitude           FLOAT            NULL,
                         longitude          FLOAT            NULL,
                         neighborhood       NVARCHAR(255)    NULL,
                         formattedAddress   NVARCHAR(255)    NULL,
                         normalizationData  NVARCHAR(MAX)    NULL,
                         crossStreets       NVARCHAR(255)    NULL,
                         observations       NVARCHAR(255)    NULL,
                         createdDate        DATETIME2        NOT NULL CONSTRAINT DF_address_createdDate DEFAULT (GETDATE()),
                         createdBy          NVARCHAR(50)     NULL,
                         modifiedDate       DATETIME2        NULL,
                         modifiedBy         NVARCHAR(50)     NULL,
                         eliminado          BIT              NOT NULL CONSTRAINT DF_address_eliminado DEFAULT (0),
                         eliminadoDate      DATETIME2        NULL,
                         eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: cities
 ******************************************************/
CREATE TABLE cities (
                        id          VARCHAR(36)      NOT NULL PRIMARY KEY,
                        name        NVARCHAR(255)    NOT NULL,
                        description NVARCHAR(255)    NULL,
                        stateId     VARCHAR(36)      NULL,
                        createdDate        DATETIME2        NOT NULL CONSTRAINT DF_cities_createdDate DEFAULT (GETDATE()),
                        createdBy          NVARCHAR(50)     NULL,
                        modifiedDate       DATETIME2        NULL,
                        modifiedBy         NVARCHAR(50)     NULL,
                        eliminado          BIT              NOT NULL CONSTRAINT DF_cities_eliminado DEFAULT (0),
                        eliminadoDate      DATETIME2        NULL,
                        eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: contact_detail
 ******************************************************/
CREATE TABLE contact_detail (
                                id           VARCHAR(36)     NOT NULL PRIMARY KEY,
                                contactType  NVARCHAR(50)    NULL,
                                entityType   NVARCHAR(50)    NULL,
                                entityId     VARCHAR(36)     NULL,
                                detailId     VARCHAR(36)     NULL,
                                principal    BIT             NOT NULL CONSTRAINT DF_contact_detail_principal DEFAULT (0),
                                createdDate        DATETIME2        NOT NULL CONSTRAINT DF_contact_detail_createdDate DEFAULT (GETDATE()),
                                createdBy          NVARCHAR(50)     NULL,
                                modifiedDate       DATETIME2        NULL,
                                modifiedBy         NVARCHAR(50)     NULL,
                                eliminado          BIT              NOT NULL CONSTRAINT DF_contact_detail_eliminado DEFAULT (0),
                                eliminadoDate      DATETIME2        NULL,
                                eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: countries
 ******************************************************/
CREATE TABLE countries (
                           id          VARCHAR(36)      NOT NULL PRIMARY KEY,
                           name        NVARCHAR(255)    NOT NULL,
                           code        NVARCHAR(10)     NULL,
                           codePhone   INT              NOT NULL,
                           description NVARCHAR(255)    NULL,
                           createdDate        DATETIME2        NOT NULL CONSTRAINT DF_countries_createdDate DEFAULT (GETDATE()),
                           createdBy          NVARCHAR(50)     NULL,
                           modifiedDate       DATETIME2        NULL,
                           modifiedBy         NVARCHAR(50)     NULL,
                           eliminado          BIT              NOT NULL CONSTRAINT DF_countries_eliminado DEFAULT (0),
                           eliminadoDate      DATETIME2        NULL,
                           eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: currencies
 ******************************************************/
CREATE TABLE currencies (
                            id              VARCHAR(36)      NOT NULL PRIMARY KEY,
                            code            NVARCHAR(10)     NOT NULL,
                            name            NVARCHAR(255)    NOT NULL,
                            symbol          NVARCHAR(10)     NULL,
                            conversionRate  FLOAT            NULL,
                            baseCurrency    BIT              NOT NULL CONSTRAINT DF_currencies_baseCurrency DEFAULT (0),
                            description     NVARCHAR(255)    NULL,
                            createdDate        DATETIME2        NOT NULL CONSTRAINT DF_currencies_createdDate DEFAULT (GETDATE()),
                            createdBy          NVARCHAR(50)     NULL,
                            modifiedDate       DATETIME2        NULL,
                            modifiedBy         NVARCHAR(50)     NULL,
                            eliminado          BIT              NOT NULL CONSTRAINT DF_currencies_eliminado DEFAULT (0),
                            eliminadoDate      DATETIME2        NULL,
                            eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: email
 ******************************************************/
CREATE TABLE email (
                       id            VARCHAR(36)      NOT NULL PRIMARY KEY,
                       emailAddress  NVARCHAR(255)    NOT NULL,
                       verified      BIT              NOT NULL CONSTRAINT DF_email_verified DEFAULT (0),
                       createdDate        DATETIME2        NOT NULL CONSTRAINT DF_email_createdDate DEFAULT (GETDATE()),
                       createdBy          NVARCHAR(50)     NULL,
                       modifiedDate       DATETIME2        NULL,
                       modifiedBy         NVARCHAR(50)     NULL,
                       eliminado          BIT              NOT NULL CONSTRAINT DF_email_eliminado DEFAULT (0),
                       eliminadoDate      DATETIME2        NULL,
                       eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: feature_toggles
 ******************************************************/
CREATE TABLE feature_toggles (
                                 id           VARCHAR(36)     NOT NULL PRIMARY KEY,
                                 featureName  NVARCHAR(255)   NOT NULL,
                                 enabled      BIT             NOT NULL CONSTRAINT DF_feature_toggles_enabled DEFAULT (0),
                                 description  NVARCHAR(255)   NULL,
                                 createdDate        DATETIME2        NOT NULL CONSTRAINT DF_feature_toggles_createdDate DEFAULT (GETDATE()),
                                 createdBy          NVARCHAR(50)     NULL,
                                 modifiedDate       DATETIME2        NULL,
                                 modifiedBy         NVARCHAR(50)     NULL,
                                 eliminado          BIT              NOT NULL CONSTRAINT DF_feature_toggles_eliminado DEFAULT (0),
                                 eliminadoDate      DATETIME2        NULL,
                                 eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: languages
 ******************************************************/
CREATE TABLE languages (
                           id         VARCHAR(36)      NOT NULL PRIMARY KEY,
                           name       NVARCHAR(255)    NOT NULL,
                           isoCode    NVARCHAR(10)     NOT NULL,
                           description NVARCHAR(255)   NULL,
                           createdDate        DATETIME2        NOT NULL CONSTRAINT DF_languages_createdDate DEFAULT (GETDATE()),
                           createdBy          NVARCHAR(50)     NULL,
                           modifiedDate       DATETIME2        NULL,
                           modifiedBy         NVARCHAR(50)     NULL,
                           eliminado          BIT              NOT NULL CONSTRAINT DF_languages_eliminado DEFAULT (0),
                           eliminadoDate      DATETIME2        NULL,
                           eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: localities
 ******************************************************/
CREATE TABLE localities (
                            id          VARCHAR(36)      NOT NULL PRIMARY KEY,
                            name        NVARCHAR(255)    NOT NULL,
                            stateId     VARCHAR(36)      NULL,
                            postalCode  NVARCHAR(50)     NULL,
                            createdDate        DATETIME2        NOT NULL CONSTRAINT DF_localities_createdDate DEFAULT (GETDATE()),
                            createdBy          NVARCHAR(50)     NULL,
                            modifiedDate       DATETIME2        NULL,
                            modifiedBy         NVARCHAR(50)     NULL,
                            eliminado          BIT              NOT NULL CONSTRAINT DF_localities_eliminado DEFAULT (0),
                            eliminadoDate      DATETIME2        NULL,
                            eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: neighborhoods
 ******************************************************/
CREATE TABLE neighborhoods (
                               id         VARCHAR(36)      NOT NULL PRIMARY KEY,
                               name       NVARCHAR(255)    NOT NULL,
                               localityId VARCHAR(36)      NOT NULL,
                               createdDate        DATETIME2        NOT NULL CONSTRAINT DF_neighborhoods_createdDate DEFAULT (GETDATE()),
                               createdBy          NVARCHAR(50)     NULL,
                               modifiedDate       DATETIME2        NULL,
                               modifiedBy         NVARCHAR(50)     NULL,
                               eliminado          BIT              NOT NULL CONSTRAINT DF_neighborhoods_eliminado DEFAULT (0),
                               eliminadoDate      DATETIME2        NULL,
                               eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: notification_templates
 ******************************************************/
CREATE TABLE notification_templates (
                                        id                  VARCHAR(36)      NOT NULL PRIMARY KEY,
                                        name                NVARCHAR(255)    NOT NULL,
                                        subject             NVARCHAR(255)    NOT NULL,
                                        body                NVARCHAR(MAX)    NOT NULL,
                                        notificationTypeId  VARCHAR(36)      NOT NULL,
                                        createdDate        DATETIME2        NOT NULL CONSTRAINT DF_notification_templates_createdDate DEFAULT (GETDATE()),
                                        createdBy          NVARCHAR(50)     NULL,
                                        modifiedDate       DATETIME2        NULL,
                                        modifiedBy         NVARCHAR(50)     NULL,
                                        eliminado          BIT              NOT NULL CONSTRAINT DF_notification_templates_eliminado DEFAULT (0),
                                        eliminadoDate      DATETIME2        NULL,
                                        eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: notification_types
 ******************************************************/
CREATE TABLE notification_types (
                                    id          VARCHAR(36)      NOT NULL PRIMARY KEY,
                                    name        NVARCHAR(255)    NOT NULL,
                                    description NVARCHAR(255)    NULL,
                                    createdDate        DATETIME2        NOT NULL CONSTRAINT DF_notification_types_createdDate DEFAULT (GETDATE()),
                                    createdBy          NVARCHAR(50)     NULL,
                                    modifiedDate       DATETIME2        NULL,
                                    modifiedBy         NVARCHAR(50)     NULL,
                                    eliminado          BIT              NOT NULL CONSTRAINT DF_notification_types_eliminado DEFAULT (0),
                                    eliminadoDate      DATETIME2        NULL,
                                    eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: parameters
 ******************************************************/
CREATE TABLE parameters (
                            parameterId          INT              NOT NULL PRIMARY KEY,
                            parameterName        NVARCHAR(100)    NOT NULL,
                            parameterValue       NVARCHAR(255)    NOT NULL,
                            parameterDescription NVARCHAR(500)    NULL,
                            parameterCategory    NVARCHAR(50)     NULL,
                            createdDate        DATETIME2        NOT NULL CONSTRAINT DF_parameters_createdDate DEFAULT (GETDATE()),
                            createdBy          NVARCHAR(50)     NULL,
                            modifiedDate       DATETIME2        NULL,
                            modifiedBy         NVARCHAR(50)     NULL,
                            eliminado          BIT              NOT NULL CONSTRAINT DF_parameters_eliminado DEFAULT (0),
                            eliminadoDate      DATETIME2        NULL,
                            eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: payment_methods
 ******************************************************/
CREATE TABLE payment_methods (
                                 id          VARCHAR(36)      NOT NULL PRIMARY KEY,
                                 name        NVARCHAR(255)    NOT NULL,
                                 description NVARCHAR(255)    NULL,
                                 enabled     BIT              NOT NULL CONSTRAINT DF_payment_methods_enabled DEFAULT (0),
                                 createdDate        DATETIME2        NOT NULL CONSTRAINT DF_payment_methods_createdDate DEFAULT (GETDATE()),
                                 createdBy          NVARCHAR(50)     NULL,
                                 modifiedDate       DATETIME2        NULL,
                                 modifiedBy         NVARCHAR(50)     NULL,
                                 eliminado          BIT              NOT NULL CONSTRAINT DF_payment_methods_eliminado DEFAULT (0),
                                 eliminadoDate      DATETIME2        NULL,
                                 eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: phone
 ******************************************************/
CREATE TABLE phone (
                       id           VARCHAR(36)      NOT NULL PRIMARY KEY,
                       phoneNumber  NVARCHAR(50)     NOT NULL,
                       extension    NVARCHAR(20)     NULL,
                       phoneType    NVARCHAR(50)     NULL,
                       countryId    VARCHAR(36)      NULL,
                       createdDate        DATETIME2        NOT NULL CONSTRAINT DF_phone_createdDate DEFAULT (GETDATE()),
                       createdBy          NVARCHAR(50)     NULL,
                       modifiedDate       DATETIME2        NULL,
                       modifiedBy         NVARCHAR(50)     NULL,
                       eliminado          BIT              NOT NULL CONSTRAINT DF_phone_eliminado DEFAULT (0),
                       eliminadoDate      DATETIME2        NULL,
                       eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: shipping_methods
 ******************************************************/
CREATE TABLE shipping_methods (
                                  id                    VARCHAR(36)      NOT NULL PRIMARY KEY,
                                  name                  NVARCHAR(255)    NOT NULL,
                                  cost                  DECIMAL(10,2)    NULL,
                                  estimatedDeliveryDays INT              NULL,
                                  description           NVARCHAR(255)    NULL,
                                  createdDate        DATETIME2        NOT NULL CONSTRAINT DF_shipping_methods_createdDate DEFAULT (GETDATE()),
                                  createdBy          NVARCHAR(50)     NULL,
                                  modifiedDate       DATETIME2        NULL,
                                  modifiedBy         NVARCHAR(50)     NULL,
                                  eliminado          BIT              NOT NULL CONSTRAINT DF_shipping_methods_eliminado DEFAULT (0),
                                  eliminadoDate      DATETIME2        NULL,
                                  eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: states
 ******************************************************/
CREATE TABLE states (
                        id          VARCHAR(36)      NOT NULL PRIMARY KEY,
                        name        NVARCHAR(255)    NOT NULL,
                        code        NVARCHAR(10)     NULL,
                        description NVARCHAR(255)    NULL,
                        countryId   VARCHAR(36)      NOT NULL,
                        createdDate        DATETIME2        NOT NULL CONSTRAINT DF_states_createdDate DEFAULT (GETDATE()),
                        createdBy          NVARCHAR(50)     NULL,
                        modifiedDate       DATETIME2        NULL,
                        modifiedBy         NVARCHAR(50)     NULL,
                        eliminado          BIT              NOT NULL CONSTRAINT DF_states_eliminado DEFAULT (0),
                        eliminadoDate      DATETIME2        NULL,
                        eliminadoBy        NVARCHAR(50)     NULL
);
GO

/******************************************************
 * TABLA: tax_settings
 ******************************************************/
CREATE TABLE tax_settings (
                              id          VARCHAR(36)      NOT NULL PRIMARY KEY,
                              name        NVARCHAR(255)    NOT NULL,
                              rate        FLOAT            NOT NULL,
                              countryId   VARCHAR(36)      NOT NULL,
                              stateId     VARCHAR(36)      NULL,
                              description NVARCHAR(255)    NULL,
                              createdDate        DATETIME2        NOT NULL CONSTRAINT DF_tax_settings_createdDate DEFAULT (GETDATE()),
                              createdBy          NVARCHAR(50)     NULL,
                              modifiedDate       DATETIME2        NULL,
                              modifiedBy         NVARCHAR(50)     NULL,
                              eliminado          BIT              NOT NULL CONSTRAINT DF_tax_settings_eliminado DEFAULT (0),
                              eliminadoDate      DATETIME2        NULL,
                              eliminadoBy        NVARCHAR(50)     NULL
);
GO
