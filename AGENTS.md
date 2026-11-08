# VetSync: Reglas para asistentes de IA

## Stack
Java 17, Spring Boot 4, Maven, JUnit 5, Mockito, MySQL (H2 en tests).

## Arquitectura
- Monolito modular en el paquete base `com.vetsync.vetsync`.
- Módulos: `clientes`, `citas`, `notificaciones`.
- Cada módulo tiene capas: `domain`, `application`, `infrastructure`, `api`.
- Un módulo NO importa clases internas de otro; se comunican por interfaces.
- Los controllers no contienen lógica de negocio.

## Convenciones
- Variables y métodos en camelCase; clases en PascalCase.
- Inyección de dependencias por constructor, nunca `@Autowired` en campos.
- Programar contra interfaces (repositorios, Notificador).
- Prohibido `catch (Exception e)` vacío; usar excepciones de dominio.
- Prohibido devolver entidades JPA desde controllers; usar DTOs.
- Validar entradas con Bean Validation.

## Pruebas (TDD)
- Primero el test, luego la implementación.
- Patrón Given-When-Then, un comportamiento por test.
- Mockito para dependencias; sin BD real en tests unitarios.
- Cobertura mínima: 80%.

## SOLID
- Una clase, una responsabilidad.
- Extender con nuevas implementaciones, no modificando las existentes.
- Inyectar dependencias; no usar `new` para servicios.

## Seguridad
- Nunca incluir contraseñas ni claves en el código.
- Consultas con parámetros, nunca concatenar SQL.
