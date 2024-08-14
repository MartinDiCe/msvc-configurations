
# Microservicio de Configuraciones (msvc-configurations)

`msvc-configurations` es un microservicio diseñado para gestionar los parámetros y configuraciones del sistema de forma centralizada. Este servicio permite la creación, actualización y eliminación de parámetros que pueden ser utilizados por otros microservicios dentro de la arquitectura.

## Funcionalidades Principales

- **Gestión de Parámetros:** Crear, actualizar, obtener y eliminar parámetros del sistema.
- **Inicialización de Parámetros Predeterminados:** Inicialización automática de parámetros críticos al iniciar la aplicación, como el estado de las entidades.
- **Interacción con Otros Microservicios:** Facilita la obtención de configuraciones clave para otros microservicios a través de endpoints expuestos.

## Tecnologías Utilizadas

- **Java** - Spring Boot
- **Spring WebFlux**
- **MongoDB** - Base de datos NoSQL para almacenamiento de parámetros.
- **Lombok** - Para reducir el código boilerplate.
- **SpringDoc** - Para la documentación de la API.
- **Reactor** - Para programación reactiva.

## Estructura del Proyecto

- **`src/main/java/com/diceprojects/msvcconfigurations`**: Código fuente del microservicio.
  - `clients`: Clases que se comunican con otros microservicios.
  - `exceptions`: Clases para el manejo de excepciones.
  - `persistences`: DTOs y entidades relacionadas con la persistencia de datos.
  - `services`: Lógica de negocio relacionada con la gestión de parámetros.
  - `security`: Configuración de seguridad y utilidades relacionadas (si aplica).
  - `controllers`: Endpoints de la API para interactuar con los parámetros.
  - `initialization`: Clases que manejan la inicialización de datos al iniciar la aplicación.
  - `utils`: Utilidades adicionales, como manejo de estados de entidades.

## Configuración

Asegúrate de configurar adecuadamente el archivo `application-dev.properties` o `application-prod.properties` para la conexión a la base de datos MongoDB y otros ajustes necesarios, como las URLs de otros microservicios con los que `msvc-configurations` interactúa.

### Ejemplo de Configuración (`application.properties`):

\`\`\`properties
spring.data.mongodb.uri=mongodb://localhost:27017/msvc-configurations
msvc.authentication.url=http://localhost:8002/api
msvc.authorization.url=http://localhost:8003/api
\`\`\`

## Uso

1. **Compilación y Ejecución:** Utiliza Maven o tu herramienta de construcción preferida para compilar y ejecutar el proyecto.
   \`\`\`bash
   mvn clean install
   java -jar target/msvc-configurations.jar
   \`\`\`

2. **Inicialización de Datos:** Al iniciar la aplicación, ciertos parámetros clave, como "EntityStatus", se inicializan automáticamente si no existen en la base de datos.

## Endpoints

- `GET /api/parameters/getParameterName/{parameterName}`: Obtiene un parámetro por su nombre.
- `POST /api/parameters`: Crea o actualiza un parámetro.
- `DELETE /api/parameters/delete/{parameterId}`: Elimina un parámetro por su ID.
- `GET /api/parameters/ListAll`: Obtiene todos los parámetros.

Documentación detallada de la API disponible en: [[DOMINIO]/apidoc/webjars/swagger-ui/index.html]([DOMINIO]/apidoc/webjars/swagger-ui/index.html)

## Contribuir

¡Contribuciones son bienvenidas! Si encuentras errores o tienes sugerencias de mejora, no dudes en abrir un issue o enviar una pull request.

## Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo `LICENSE.md` para más detalles.

## Dependencias

Este proyecto utiliza las siguientes dependencias principales:

- **Spring Boot Starter Data MongoDB Reactive**
- **Spring Boot Starter WebFlux**
- **Lombok**
- **SpringDoc OpenAPI WebFlux UI**
- **Reactor Core**

Las dependencias están definidas en el archivo `pom.xml` del proyecto.
