# Informe de Proyecto: Sistema de Gestión de Biblioteca Universitaria "Híbrida" (Avance 2)

Este documento detalla el cumplimiento de los requerimientos técnicos para el segundo avance del proyecto, enfocándose en la persistencia clásica con SQLite y el ciclo de vida completo del préstamo.

## 1. Persistencia de Datos (SQLite Clásico)
Siguiendo los requerimientos del curso, se ha migrado la base de datos a una implementación manual utilizando `SQLiteOpenHelper`.

*   **Clase DatabaseHelper**: Implementa `onCreate()` y `onUpgrade()`.
*   **Gestión Manual**: Todas las operaciones de inserción, actualización y eliminación se realizan mediante `ContentValues` y consultas parametrizadas (`selectionArgs`), evitando la concatenación de strings para prevenir inyecciones SQL.
*   **Estructura**:
    *   `usuarios`: Almacena perfiles (id, nombre, cédula, correo, password, fotoUri).
    *   `categorias`: Entidad relacionada que clasifica los libros.
    *   `libros`: Registra los préstamos vinculando usuario y categoría.

## 2. CRUD Completo (Entidad Principal: Préstamos)
Se ha implementado el ciclo de vida completo para la gestión de libros:
*   **CREATE**: Desde el detalle del libro, el usuario selecciona una categoría y solicita el préstamo.
*   **READ**: El Perfil muestra un `RecyclerView` con todos los libros activos, alimentado mediante un `JOIN`.
*   **UPDATE (Edición)**: Se permite editar la categoría de un préstamo existente mediante un diálogo con un `Spinner`.
*   **DELETE (Eliminación)**: Se ha implementado la eliminación física de registros con un **Diálogo de Confirmación** previo.

## 3. Relación de Entidades y JOIN
*   **Entidad Relacionada**: La tabla `categorias` está poblada inicialmente con 5 materias (Matemáticas, Programación, etc.).
*   **Uso de Spinner**: Al solicitar un préstamo o editarlo, las categorías se cargan dinámicamente desde la base de datos en un `Spinner`.
*   **Consulta JOIN**: El listado de "Mis Préstamos" utiliza un `INNER JOIN` para mostrar el nombre legible de la categoría en lugar de su identificador numérico.

## 4. Navegación y UI Moderna
*   **Navigation Drawer**: Menú lateral para acceso fluido.
*   **Material 3**: Uso de Cards, Chips y Outlined Boxes.
*   **Multimedia**: Capacidad de subir una foto de perfil desde la galería y persistirla en la base de datos.

## 5. Datos de Prueba y Persistencia
*   **Carga Inicial**: El sistema detecta la primera instalación y pre-carga un usuario `admin` (pass: `1234`) con 8 préstamos y 5 categorías para demostración inmediata.
*   **Verificación de Persistencia**: Los datos se almacenan en el almacenamiento interno del dispositivo, garantizando que sobrevivan al cierre completo de la aplicación.

## 6. Conclusión
La aplicación cumple con el 100% de los puntos solicitados en la rúbrica del Avance 2, integrando una arquitectura robusta (MVVM) con el manejo clásico de bases de datos relacionales en Android.
