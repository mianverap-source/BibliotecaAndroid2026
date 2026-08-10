# Informe de Proyecto: Aplicación Móvil "Biblioteca Digital"

Este documento explica el proceso de desarrollo, la estructura y las tecnologías utilizadas para crear esta aplicación de gestión de biblioteca.

## 1. Introducción
El objetivo del proyecto fue desarrollar una aplicación funcional para Android que permita a los estudiantes registrarse, buscar libros en una base de datos global y gestionar sus préstamos personales de forma local.

## 2. Estructura y Navegación
La aplicación consta de **5 ventanas (Fragmentos)** principales, organizadas en un flujo lógico de usuario:

1.  **Login**: Pantalla de acceso que valida las credenciales y mantiene la sesión abierta.
2.  **Registro**: Formulario completo para nuevos usuarios con validación de datos (Nombre, Cédula, Correo, Celular, Institución, etc.).
3.  **Catálogo**: Pantalla principal con una barra de búsqueda para encontrar libros en tiempo real.
4.  **Detalle del Libro**: Muestra la información específica y la descripción completa del libro seleccionado.
5.  **Perfil**: Ficha del usuario donde se muestran sus datos y la lista de libros que tiene prestados.

## 3. Base de Datos Local
Para la persistencia de datos, implementé **Room Database (SQLite)**. El diseño se basa en dos entidades principales con una relación de 1 a N:

*   **Tabla Usuarios**: Almacena toda la información del perfil y la contraseña encriptada.
*   **Tabla Libros**: Registra los libros que cada usuario decide prestar, vinculándolos mediante una clave foránea (`usuarioId`).

### Diagrama Lógico
*   **Usuario** (1) ---- (N) **Libros Prestados**

## 4. Integración de API Externa
Para no depender de una base de datos estática, integré la API REST de **Open Library**. 
*   **Retrofit**: Utilizado para realizar las peticiones HTTP de forma eficiente.
*   **Coil**: Librería usada para cargar y mostrar las portadas de los libros directamente desde los servidores de Open Library.
*   **Funcionalidad**: Permite buscar cualquier libro por título o autor y obtener su descripción completa al instante.

## 5. Pasos del Desarrollo
1.  **Diseño de UI**: Creación de los layouts en XML usando Material Design.
2.  **Configuración de Room**: Definición de Entidades y DAOs para el manejo de datos locales.
3.  **Capa de Red**: Implementación de Retrofit para conectar con Open Library.
4.  **Arquitectura MVVM**: Separación de la lógica en ViewModels para que la app sea escalable y fácil de mantener.
5.  **Sesión**: Uso de SharedPreferences para que el usuario no tenga que loguearse cada vez que abre la app.

## 6. Conclusión
La aplicación es totalmente funcional, permitiendo un flujo completo desde el registro de un nuevo estudiante hasta la gestión de sus propios préstamos de libros, todo apoyado en una base de datos global.
