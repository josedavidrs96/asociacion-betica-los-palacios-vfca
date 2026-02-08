# Asociación Bética Los Palacios - Sistema de Gestión de Pases

Este proyecto es una aplicación web para gestionar pases de autobús y su validación mediante códigos QR.
Está diseñado para desplegarse en **Vercel** utilizando una arquitectura serverless.

## Estructura del Proyecto

- `frontend/`: Aplicación Angular (SPA).
- `api/`: Funciones serverless (Node.js/TypeScript) que actúan como backend.
- `db/`: Esquema de la base de datos (PostgreSQL).

## Requisitos

- Node.js 18+
- Cuenta en Vercel (para despliegue y base de datos Postgres/Neon).

## Configuración Local

1.  **Instalar dependencias:**
    ```bash
    npm install
    cd frontend
    npm install
    cd ..
    ```

2.  **Configurar variables de entorno:**
    Crea un archivo `.env` en la raíz con las siguientes variables (obtenidas de Vercel):
    ```env
    POSTGRES_URL="..."
    POSTGRES_PRISMA_URL="..."
    POSTGRES_URL_NO_SSL="..."
    POSTGRES_URL_NON_POOLING="..."
    POSTGRES_USER="..."
    POSTGRES_HOST="..."
    POSTGRES_PASSWORD="..."
    POSTGRES_DATABASE="..."
    AUTH_JWT_SECRET="tu_secreto_super_seguro_para_jwt"
    ```

3.  **Ejecutar en desarrollo:**
    Utiliza Vercel CLI para simular el entorno serverless localmente:
    ```bash
    npx vercel dev
    ```
    La aplicación estará disponible en `http://localhost:3000`.

## Despliegue

El proyecto está configurado para desplegarse automáticamente en Vercel al hacer push a la rama principal.
Asegúrate de configurar las variables de entorno en el panel de Vercel.

## Funcionalidades

- **Login:** Acceso para Administradores y Validadores.
- **Admin:**
    - Gestión de usuarios (crear otros admins o validadores).
    - Generación de pases (códigos únicos).
    - Visualización de pases y su estado.
- **Validación:**
    - Interfaz para validar códigos de pases.
    - Control de usos restantes y caducidad.

## Base de Datos

El esquema se encuentra en `db/DatabaseSchema.sql`. Debe ejecutarse en la base de datos Postgres conectada al proyecto Vercel.
