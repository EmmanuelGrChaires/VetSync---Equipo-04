# VetSync - Sistema de Gestión de Citas Veterinarias

Backend en Java para registrar dueños y mascotas con su historial médico, agendar citas sin choques de horario para los veterinarios y enviar recordatorios (correo simulado).



## Tecnologías

| Componente | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.2 |
| Maven (wrapper incluido) | `mvnw` |
| Base de datos | MySQL 8.0 (H2 en memoria para tests) |
| Pruebas | JUnit 5, Mockito, MockMvc |
| Cobertura | JaCoCo (mínimo 80 % de líneas) |
| CI | GitHub Actions |

## Arquitectura

Monolito modular, desacoplado por interfaces. Cada módulo tiene sus capas `domain`, `application`, `infrastructure` y `api`.

```
com.vetsync.vetsync
├── clientes         dueños, mascotas, historial médico
├── citas            veterinarios y agendamiento de citas
├── notificaciones   recordatorios por correo (simulado)
└── shared           manejo de errores de la API
```

Decisiones de diseño:

- **Los módulos no importan clases de otros módulos.** `Cita` guarda `mascotaId` como número y consulta si la mascota existe mediante la interfaz `MascotaConsulta`. `notificaciones` usa `CitaProxima`, un objeto propio, en lugar de la entidad `Cita`.
- **La lógica de negocio es Java puro**, sin anotaciones de Spring (`AgendarCitaService`, `RegistroClienteService`, `RecordatorioCitaService`). Se ensambla en las clases `Configuracion*`.
- **Dependencias por constructor** y contra interfaces (`CitaRepository`, `ClienteRepository`, `Notificador`...).
- **El tiempo se inyecta con `Clock`**, de modo que los tests no dependen de la hora real.
- Las reglas del proyecto para asistentes de IA están en [`AGENTS.md`](AGENTS.md).

## Modelo de datos

Tablas: `duenos`, `mascotas`, `veterinarios`, `citas`, `historial_medico`, `notificaciones`. Hibernate valida y completa el esquema con `ddl-auto=update`.

## Cómo ejecutarlo

### 1. Requisitos

- JDK 17 o superior
- MySQL 8 en `localhost:3306`

### 2. Crear la base de datos

```sql
CREATE DATABASE vetsync CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configurar la contraseña (no se sube al repositorio)

La contraseña de MySQL va en un archivo local que está en `.gitignore`. Crea `src/main/resources/application-local.properties`:

```properties
spring.datasource.password=TU_CONTRASEÑA
```

`application.properties.example` muestra la configuración completa de referencia.

### 4. Insertar un veterinario

Todavía no hay endpoint para crear veterinarios, así que se insertan por SQL:

```sql
INSERT INTO veterinarios (nombre, especialidad, email)
VALUES ('Dra. Lopez', 'General', 'lopez@vetsync.com');
```

### 5. Arrancar

```
mvnw spring-boot:run
```

La API queda en `http://localhost:8080`.

## Pruebas y cobertura

```
mvnw test      # corre los tests y genera el reporte
mvnw verify    # además exige el mínimo de cobertura (80 %)
```

El reporte HTML queda en `target/site/jacoco/index.html`.

Los tests usan H2 en memoria (`src/test/resources/application.properties`), por lo que no tocan MySQL.

**Qué se excluye del cálculo de cobertura y por qué:**

- Las entidades JPA (`Dueno`, `Mascota`, `HistorialMedico`, `Veterinario`, `Cita`, `Notificacion`): solo contienen datos y accesores.
- `VetsyncApplication`: clase de arranque.
- Las clases `Configuracion*`: solo ensamblan beans y no tienen lógica.

La lógica de negocio (`*.application`) tiene 100 % de cobertura. Las consultas SQL nativas (`MascotaConsultaJpa`, `CitasProximasConsultaJpa`) no tienen test de integración todavía.

## Integración continua

El workflow `.github/workflows/ci.yml` corre `mvnw verify` en cada push y pull request a `main`. Falla si algún test falla o si la cobertura baja del 80 %. Publica el reporte de JaCoCo como artefacto.

## Documentación de la API

Base URL: `http://localhost:8080`. Todas las peticiones y respuestas son JSON. Las fechas usan formato ISO (`2026-11-20T10:00:00`).

### Registrar un dueño

`POST /api/duenos`

| Campo | Tipo | Obligatorio |
|---|---|---|
| `nombre` | string | sí |
| `email` | string (único) | sí |
| `telefono` | string | no |

```json
{ "nombre": "Ana", "email": "ana@mail.com", "telefono": "555" }
```

Respuesta `201 Created`:

```json
{ "id": 1, "nombre": "Ana", "email": "ana@mail.com", "telefono": "555" }
```

### Registrar una mascota

`POST /api/duenos/{duenoId}/mascotas`

| Campo | Tipo | Obligatorio |
|---|---|---|
| `nombre` | string | sí |
| `especie` | string | sí |
| `raza` | string | no |
| `fechaNacimiento` | fecha (`2020-05-01`) | no |

```json
{ "nombre": "Firulais", "especie": "Perro", "raza": "Mestizo" }
```

Respuesta `201 Created`:

```json
{ "id": 1, "duenoId": 1, "nombre": "Firulais", "especie": "Perro" }
```

### Agregar entrada al historial médico

`POST /api/mascotas/{mascotaId}/historial`

| Campo | Tipo | Obligatorio |
|---|---|---|
| `fecha` | fecha | sí |
| `diagnostico` | string | sí |
| `tratamiento` | string | no |
| `citaId` | número | no |

```json
{ "fecha": "2026-11-08", "diagnostico": "Otitis", "tratamiento": "Gotas" }
```

Respuesta `201 Created`:

```json
{ "id": 1, "mascotaId": 1, "fecha": "2026-11-08", "diagnostico": "Otitis" }
```

### Agendar una cita

`POST /api/citas`

| Campo | Tipo | Obligatorio |
|---|---|---|
| `mascotaId` | número | sí |
| `veterinarioId` | número | sí |
| `inicio` | fecha y hora | sí |
| `fin` | fecha y hora | sí |
| `motivo` | string | no |

```json
{
  "mascotaId": 1,
  "veterinarioId": 1,
  "inicio": "2026-11-20T10:00:00",
  "fin": "2026-11-20T10:30:00",
  "motivo": "Vacuna"
}
```

Respuesta `201 Created`:

```json
{
  "id": 1, "mascotaId": 1, "veterinarioId": 1,
  "inicio": "2026-11-20T10:00:00", "fin": "2026-11-20T10:30:00",
  "estado": "PROGRAMADA"
}
```

Reglas de negocio:

1. `fin` debe ser posterior a `inicio`.
2. La cita no puede estar en el pasado.
3. La mascota debe existir.
4. El veterinario no puede tener otra cita (no cancelada) que se traslape con el horario.

### Códigos de error

| Código | Cuándo | Cuerpo |
|---|---|---|
| `400 Bad Request` | Datos inválidos de negocio: cita en el pasado, fin anterior al inicio, mascota o veterinario inexistente, dueño inexistente, email repetido o vacío, diagnóstico vacío | `{ "error": "mensaje" }` |
| `409 Conflict` | El veterinario ya tiene una cita que se traslapa con ese horario | `{ "error": "El veterinario ya tiene una cita en ese horario" }` |

### Recordatorios

Cada hora, una tarea programada busca citas `PROGRAMADA` que empiecen en las próximas 24 horas y envía un recordatorio por correo **simulado** (se escribe en el log). La tabla `notificaciones` evita enviar dos veces el aviso de la misma cita.

## Limitaciones conocidas

- **Sin autenticación ni autorización.** Cualquiera que llegue al puerto 8080 puede usar la API, y el historial médico es un dato sensible. Siguiente paso: Spring Security.
- **Condición de carrera al agendar.** La validación de traslape y el guardado son dos pasos separados. Dos solicitudes simultáneas con horarios que se traslapan parcialmente podrían pasar ambas. El `UNIQUE (veterinario_id, inicio)` de la base solo atrapa inicios idénticos. Solución futura: bloqueo pesimista o restricción a nivel de base de datos.
- **Validación de entrada básica.** Los DTO validan campos obligatorios, pero no el formato del email ni la longitud máxima de los campos. Los errores de validación de Bean Validation y los JSON mal formados no tienen un manejador propio.
- **Relaciones `@ManyToOne` en modo EAGER.** Sin impacto hoy (no hay listados), pero podría causar consultas N+1 si se agregan endpoints de listado.
- **Sin endpoint para veterinarios**: se insertan por SQL.
- **Sin test de integración** para las consultas SQL nativas de `MascotaConsultaJpa` y `CitasProximasConsultaJpa`.
- **El CI no ejecuta un linter real**; verifica compilación, tests y cobertura.

## Uso de IA en el desarrollo

El proyecto se construyó con asistencia de IA (Claude) siguiendo las reglas de `AGENTS.md`: arquitectura y esquema, entidades, tests (TDD), refactor con `Clock`, módulo de notificaciones, revisión de seguridad y rendimiento, CI y esta documentación. Los prompts, las respuestas y la crítica de cada uno están en la bitácora de prompts entregada aparte.

## Equipo

Equipo 04: 
- Emmanuel Guadalupe Ruvalcaba Chaires
- Edith Guadalupe Ramirez Quiñones
- Juan Antonio Valencia Sánchez
- Jesús Alfredo Leonardo Elías
- Ana Lila Ibarra Gámez
