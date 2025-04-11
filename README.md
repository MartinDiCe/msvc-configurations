# Microservicio de Configuraciones (msvc-configurations)

`msvc-configurations` es un microservicio diseñado para gestionar los parámetros y configuraciones del sistema de forma centralizada. Este servicio permite la creación, actualización y eliminación de parámetros que pueden ser utilizados por otros microservicios dentro de la arquitectura.

## Funcionalidades Principales

- **Gestión de Parámetros:** Creación, actualización, consulta y eliminación de parámetros del sistema.
- **Inicialización de Parámetros Predeterminados:** Inicialización automática de parámetros críticos al iniciar la aplicación, por ejemplo, "EntityStatus", "TokenTimeOut", "EmailDomains", etc.
- **Registro y Trazabilidad de API:** Captura de trazas de las solicitudes y respuestas de la API (incluyendo request body y response body) mediante un filtro personalizado. La traza se publica de forma asíncrona y se persiste en la base de datos solo cuando el parámetro `ApiTraceActive` está configurado en `true`.
- **Interacción con Otros Microservicios:** Provee endpoints para la consulta de parámetros y configuraciones clave que otros microservicios puedan necesitar.

## Tecnologías Utilizadas

- **Java** (Spring Boot)
- **Spring WebFlux** – para la programación reactiva
- **Spring Data R2DBC** – (o MongoDB, según la configuración) para el almacenamiento reactivo de datos
- **Lombok** – para reducir el código boilerplate
- **SpringDoc OpenAPI** – para la documentación de la API
- **Reactor** – para la programación reactiva
- **SLF4J / Logback** – para logging (con una implementación personalizada `AppLogger`)

## Estructura del Proyecto

- **`src/main/java/com/diceprojects/msvcconfigurations`**: Código fuente del microservicio.
    - **`clients`**: Clases para la comunicación con otros microservicios.
    - **`controllers`**: Endpoints de la API para interactuar con los parámetros y configuraciones.
    - **`exceptions`**: Manejo de excepciones personalizadas.
    - **`helpers`**: Utilidades adicionales, como los decoradores de request y response (por ejemplo, `CachedBodyServerHttpRequest` y `ResponseCaptureDecorator`).
    - **`initialization`**: Clases que gestionan la inicialización automática de parámetros predeterminados al arranque.
    - **`logging`**: Interfaz y clases para el registro personalizado (`AppLogger`).
    - **`middleware`**: Filtros web para capturar el body de la solicitud y la respuesta, y para registrar las trazas de las API.
    - **`persistences`**: Entidades y DTOs que representan los parámetros y otras configuraciones.
    - **`services`**: Lógica de negocio para la gestión de parámetros y trazas.
    - **`traces`**: Implementación del bus de trazas (`ApiTraceBus`) y sus suscriptores.

## Configuración

Asegúrate de configurar correctamente el archivo `application-dev.properties` o `application-prod.properties` con la conexión a la base de datos y otros ajustes necesarios (URLs de otros microservicios, tiempo de espera, etc.).


## Uso

### Compilación y Ejecución

Utiliza Maven (o tu herramienta de construcción preferida) para compilar y ejecutar el proyecto:

```bash
mvn clean install
java -jar target/msvc-configurations.jar
```

### Inicialización de Parámetros

Al iniciar la aplicación, se ejecuta el proceso de inicialización para asegurarse de que todos los parámetros predeterminados estén en la base de datos (por ejemplo, ApiTraceActive, TokenTimeOut, EmailDomains, etc.).

### Consulta de la API

**Gestión de Parámetros:**

- `GET /api/parameters/getParameterName/{parameterName}`
- `POST /api/parameters`
- `DELETE /api/parameters/delete/{parameterId}`
- `GET /api/parameters/ListAll`

**Registro de Trazas:**

Las trazas de las API se capturan en segundo plano y se persisten automáticamente si el parámetro `ApiTraceActive` está en `true`.

**Documentación de la API:**

La documentación interactiva de la API está disponible en:
```
http://[DOMINIO]:[PORT]/apidoc/swagger-ui/index.html
```

### Ejemplo de Tramas (API Traces)

Al registrar una traza, la entidad ApiTrace almacenará la siguiente información (ejemplo):

```json
{
  "name": "Argentina",
  "code": "AR",
  "codePhone": "54",
  "description": "Argentina",
  "timeZone": "UTC-3"
}
```

**Respuesta esperada:**

```json
{
  "id": "8e450f20-cc4f-4455-bc5d-5c2d2a8ddd45",
  "name": "Argentina",
  "code": "AR",
  "codePhone": 54,
  "description": "Argentina",
  "timeZone": "UTC-3"
}
```

- `executionTimeSeconds`: 0.135
- `payloadSize`: 136 (en bytes)
- `httpResponseCode`: 201

## Contribución

¡Se agradecen las contribuciones! Si encuentras errores o tienes sugerencias de mejora, no dudes en abrir un issue o enviar una pull request.

## Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo LICENSE.md para más detalles.

## Dependencias

Las principales dependencias utilizadas en este proyecto son:

- Spring Boot Starter Data MongoDB Reactive (o Spring Boot Starter Data R2DBC, según la configuración)
- Spring Boot Starter WebFlux
- Lombok
- SpringDoc OpenAPI WebFlux UI
- Reactor Core
- SLF4J / Logback

Las dependencias están definidas en el archivo `pom.xml`.
