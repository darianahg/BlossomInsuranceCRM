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

La base de datos se guarda en la carpeta `./data` (archivo `segurosdb.mv.db`),
así que tus datos persisten aunque reinicies la app. Si quieres reiniciar todo
desde cero, borra esa carpeta `data`.

También puedes ver las tablas directamente en el navegador entrando a
`http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/segurosdb`,
usuario `sa`, sin contraseña).

## Estructura del proyecto

```
src/main/java/com/clienthub/seguros/
 ├── model/          Entidades: User, Client, Contract + enums (Role, InsuranceType, ContractStatus)
 ├── repository/     Interfaces JPA (acceso a la base de datos)
 ├── service/        Lógica de negocio (incluye el filtro admin/agente)
 ├── controller/      Rutas web (login, dashboard, clientes, contratos)
 └── config/         Seguridad (login/roles) y datos de ejemplo al arrancar

src/main/resources/
 ├── templates/       Vistas Thymeleaf (login, dashboard, clientes, contratos)
 └── application.properties   Configuración de base de datos y del servidor
```

## Cómo funciona el control de acceso admin vs agente

- Cada **Cliente** tiene un campo `assignedAgent` (el agente responsable).
- Un **Contrato** siempre pertenece a un **Cliente**.
- El **Admin** ve todos los clientes y contratos sin filtro.
- Un **Agente**, al iniciar sesión, solo ve los clientes que tiene asignados y
  los contratos de esos clientes (filtrado automático en `ClientService` /
  `ContractService`, método `findVisibleClients()` / `findVisibleContracts()`).
- Cuando un agente crea un cliente nuevo, el sistema se lo auto-asigna.
- Solo el admin puede reasignar el agente responsable de un cliente desde el formulario.

## Cómo migrar de H2 a SQL Server más adelante

En `application.properties` están comentadas las 3 líneas que hay que cambiar,
más agregar la dependencia del driver de SQL Server en el `pom.xml`. El resto
del código (entidades, repositorios, servicios) no cambia, porque usa JPA.

## Funciones incluidas

- **Login con roles**: Administrador y Agente, con pantalla de carga "Blossom" al entrar.
- **Gestión de clientes** (CRUD): visible para admin (todos) y agente (solo los suyos).
- **Gestión de contratos/pólizas** (CRUD): tipo de seguro, aseguradora, prima, vigencia, estado.
- **Gestión de usuarios** (solo Admin): crear agentes nuevos y darles sus credenciales,
  activar/desactivar cuentas. Ruta: `/users`.
- **Reportes exportables**: botón "Exportar reporte (CSV)" en Clientes y en Contratos,
  se abre directo en Excel.
- **Dashboard**: totales de clientes/contratos y alertas de contratos por vencer en 30 días.
- **Tema Blossom**: paleta rosa/magenta y tipografía "Pacifico" para la marca, definidos en
  `src/main/resources/static/css/blossom.css` (para cambiar colores, edita las variables
  `--blossom-*` al inicio del archivo).

## Próximos pasos sugeridos (si te sobra tiempo)

- Alertas automáticas por correo cuando un contrato está por vencer (Spring Scheduler + Mail).
- Módulo de pagos/cuotas por contrato.
- Gráfica en el dashboard con Chart.js (ej. contratos por tipo de seguro).
- Exportar reportes a PDF/Excel.
