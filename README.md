# Asociación Bética Los Palacios VFCA

Aplicación para la gestión de socios y transporte en autobús de la Peña Bética de Los Palacios y Villafranca.

## Despliegue en Vercel (modo "todo Vercel")
- **Angular (Standalone SPA)** en `frontend/`, compila a `/public`
- **API serverless** en `/api`
- **Base de datos**: Vercel Postgres (Storage)

> El backend Spring Boot/SQLite queda como **legado** y no se usa para el despliegue en Vercel.

## Roles
- `ADMIN`: gestión completa
- `VALIDATOR`: validación de pases (móvil del bus)
- `ABONADO`: reservado para evolución futura

## Requisitos en Vercel (obligatorio)
1. Storage → **Postgres** → crea DB y conéctala al proyecto
2. Ejecuta `db/schema.sql` en la consola de queries de la DB
3. Variables de entorno:
   - `AUTH_JWT_SECRET` = `<<pon-un-secreto-largo-aqui>>`

## Desarrollo local
Instalar dependencias:

## Cómo ejecutar
```bash
mvn spring-boot:run
```

## Próximos pasos
- Añadir interfaz web sencilla para administración
- Generación de QR para usuarios
- Control de accesos en autobús
