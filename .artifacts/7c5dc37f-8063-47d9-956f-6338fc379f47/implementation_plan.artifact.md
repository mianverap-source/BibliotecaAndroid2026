# Implementación de API REST (Open Library) y Navegación

Este plan detalla la integración de la API de Open Library para obtener libros reales y completar el flujo de navegación con la pantalla de detalles.

## Cambios Propuestos

### Configuración de Dependencias

#### [MODIFY] [libs.versions.toml](file:///C:/Users/User/AndroidStudioProjects/biblioteca/gradle/libs.versions.toml)
* Agregar versiones y librerías para Retrofit, Gson y Coil (carga de imágenes).

#### [MODIFY] [build.gradle.kts (app)](file:///C:/Users/User/AndroidStudioProjects/biblioteca/app/build.gradle.kts)
* Agregar las dependencias de Retrofit y Coil.

---

### Capa de Red (API REST)

#### [NEW] [OpenLibraryModels.kt](file:///C:/Users/User/AndroidStudioProjects/biblioteca/app/src/main/java/com/example/biblioteca/data/remote/OpenLibraryModels.kt)
* Clases de datos para mapear la respuesta JSON de Open Library.

#### [NEW] [OpenLibraryService.kt](file:///C:/Users/User/AndroidStudioProjects/biblioteca/app/src/main/java/com/example/biblioteca/data/remote/OpenLibraryService.kt)
* Interfaz de Retrofit para definir los endpoints de búsqueda y detalle.

#### [NEW] [RetrofitClient.kt](file:///C:/Users/User/AndroidStudioProjects/biblioteca/app/src/main/java/com/example/biblioteca/data/remote/RetrofitClient.kt)
* Singleton para proveer la instancia de Retrofit.

---

### Interfaz de Usuario (Catálogo y Detalle)

#### [NEW] [CatalogoViewModel.kt](file:///C:/Users/User/AndroidStudioProjects/biblioteca/app/src/main/java/com/example/biblioteca/ui/catalogo/CatalogoViewModel.kt)
* Manejar la petición a la API y el estado de la lista de libros.

#### [MODIFY] [LibroAdapter.kt](file:///C:/Users/User/AndroidStudioProjects/biblioteca/app/src/main/java/com/example/biblioteca/ui/catalogo/LibroAdapter.kt)
* Actualizar para usar Coil y mostrar la portada desde una URL.

#### [MODIFY] [fragment_detalle_libro.xml](file:///C:/Users/User/AndroidStudioProjects/biblioteca/app/src/main/res/layout/fragment_detalle_libro.xml)
* Diseñar la pantalla de detalles (Imagen grande, título, autor, descripción).

#### [MODIFY] [DetalleLibroFragment.kt](file:///C:/Users/User/AndroidStudioProjects/biblioteca/app/src/main/java/com/example/biblioteca/ui/detalle/DetalleLibroFragment.kt)
* Implementar la lógica para recibir el libro seleccionado y mostrar su información.

## Plan de Verificación

### Verificación Manual
1. Abrir la app e ir al Catálogo.
2. Comprobar que los libros se cargan desde internet (requiere conexión).
3. Verificar que las imágenes de portada se visualizan.
4. Tocar un libro y verificar que se abre la pantalla de detalles con la información correcta.
