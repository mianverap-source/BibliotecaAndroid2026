# Informe de Proyecto: Sistema de Gestión de Biblioteca Universitaria "Híbrida"

Este documento detalla el desarrollo de una solución móvil avanzada para la gestión de recursos bibliográficos tanto físicos como digitales.

## 1. Concepto del Proyecto
La aplicación ha sido diseñada para simular un entorno real de biblioteca universitaria, donde los estudiantes no solo consultan un catálogo, sino que interactúan con el inventario físico del campus y acceden a recursos digitales.

## 2. Navegación y Experiencia de Usuario
Se implementó un flujo moderno basado en **Material 3**:
*   **Navigation Drawer**: Menú lateral para acceso rápido a Perfil y Catálogo.
*   **Filtros Inteligentes**: Uso de **Chips** en el catálogo para filtrar libros por categorías educativas (Matemáticas, Programación, etc.).
*   **Confirmación de Procesos**: Retroalimentación visual mediante Toasts al registrarse o solicitar préstamos.

## 3. Gestión de Recursos (Físico vs Digital)
Una de las innovaciones del proyecto es la lógica de diferenciación de recursos:

### Recursos Digitales
*   **Acceso Directo**: Los libros marcados como digitales permiten la **descarga inmediata** en formato PDF (simulado).
*   **Iconografía**: Identificados con un icono de nube.

### Recursos Físicos
*   **Ubicación Real**: Muestra el **Piso, Módulo y Estante** donde se encuentra el ejemplar dentro de la biblioteca.
*   **Control de Inventario**: Muestra el número de copias totales y cuántas están disponibles para préstamo.
*   **Reserva**: Si hay stock, el estudiante puede "Reservar" el libro para retirarlo en ventanilla.

## 4. Diseño de Base de Datos (Relación 1 a N)
Para la persistencia local se utilizó **Room Database** con una arquitectura de relación de uno a muchos.

### Estructura de Tablas
1. **Tabla `usuarios`**: Almacena el perfil del estudiante, incluyendo la ruta de su foto de perfil (`fotoUri`).
2. **Tabla `libros`**: Gestiona los préstamos. Cada registro está vinculado a un usuario mediante `usuarioId`. Incluye campos para la fecha de préstamo y el estado de devolución (`isDevuelto`).

### Diccionario Técnico
*   **Clave Primaria (PK)**: `id` en ambas tablas.
*   **Clave Foránea (FK)**: `usuarioId` en la tabla libros, que apunta a `usuarios.id`.
*   **Restricción**: Se configuró un borrado en cascada parcial (`SET_NULL`) para mantener la integridad de los datos.

## 5. Tecnologías y Arquitectura
*   **Base de Datos (Room)**: Gestión local de perfiles de usuario, fotos y estado de los libros prestados (Versión 2 con migración).
*   **Red (Retrofit & Open Library)**: Sincronización con una base de datos global de libros en tiempo real.
*   **Imágenes (Coil)**: Carga eficiente de portadas y fotos de perfil desde la galería o internet.
*   **Arquitectura MVVM**: Lógica desacoplada para facilitar la escalabilidad del sistema.

## 6. Conclusión
El sistema resultante es una herramienta integral que demuestra la capacidad de gestionar datos complejos y procesos de negocio reales de una institución educativa, integrando APIs externas con persistencia local robusta.
