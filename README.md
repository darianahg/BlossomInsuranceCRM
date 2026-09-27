# 🌸 Blossom — CRM de Seguros

CRM web simple enfocado en el manejo de **contratos (pólizas)** de seguros, con
login por roles (Administrador / Agente) y tema visual propio.

## Stack

- Java 17 + Spring Boot 3.3
- Spring Data JPA + Spring Security
- Thymeleaf + Bootstrap 5 (sin build de frontend, todo por CDN)
- Base de datos H2 (archivo local, **no requiere instalar nada** para empezar)

## Cómo correrlo

1. Requisitos: JDK 17 y Maven instalados (o abrir el proyecto directo en IntelliJ IDEA,
   que trae Maven integrado).
2. Abre la carpeta `crm-seguros` en IntelliJ como proyecto Maven, o desde consola:
   ```
   mvn spring-boot:run
   ```
3. Entra en el navegador a: **http://localhost:8080**
4. Usuarios de prueba (se crean solos la primera vez que arranca la app):
   - **admin / admin123** → rol Administrador, ve todos los clientes y contratos.
   - **agente1 / agente123** → rol Agente, solo ve los clientes que tiene asignados.

