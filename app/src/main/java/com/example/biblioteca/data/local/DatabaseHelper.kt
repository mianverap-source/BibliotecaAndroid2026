package com.example.biblioteca.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.biblioteca.data.Libro
import com.example.biblioteca.data.local.entities.Categoria
import com.example.biblioteca.data.local.entities.Usuario

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "biblioteca_universitaria.db"
        private const val DATABASE_VERSION = 4

        const val TABLE_USUARIOS = "usuarios"
        const val TABLE_CATEGORIAS = "categorias"
        const val TABLE_LIBROS = "libros"

        const val COL_U_ID = "id"
        const val COL_U_NOMBRE = "nombreCompleto"
        const val COL_U_CEDULA = "cedula"
        const val COL_U_CORREO = "correo"
        const val COL_U_PASSWORD = "password"
        const val COL_U_INSTITUCION = "institucion"
        const val COL_U_TELEFONO = "telefono"
        const val COL_U_DIRECCION = "direccion"
        const val COL_U_ANIO = "anioIngreso"
        const val COL_U_FOTO = "fotoUri"

        const val COL_C_ID = "id"
        const val COL_C_NOMBRE = "nombre"

        const val COL_L_ID = "id"
        const val COL_L_TITULO = "titulo"
        const val COL_L_AUTOR = "autor"
        const val COL_L_USUARIO_ID = "usuarioId"
        const val COL_L_CATEGORIA_ID = "categoriaId"
        const val COL_L_FECHA = "fechaPrestamo"
        const val COL_L_VENCIMIENTO = "fechaVencimiento"
        const val COL_L_DEVUELTO = "isDevuelto"
        const val COL_L_COMENTARIO = "comentario"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsuarios = ("CREATE TABLE $TABLE_USUARIOS (" +
                "$COL_U_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_U_NOMBRE TEXT NOT NULL, " +
                "$COL_U_CEDULA TEXT UNIQUE NOT NULL, " +
                "$COL_U_CORREO TEXT UNIQUE NOT NULL, " +
                "$COL_U_PASSWORD TEXT NOT NULL, " +
                "$COL_U_INSTITUCION TEXT, " +
                "$COL_U_TELEFONO TEXT, " +
                "$COL_U_DIRECCION TEXT, " +
                "$COL_U_ANIO INTEGER, " +
                "$COL_U_FOTO TEXT)")
        db.execSQL(createUsuarios)

        val createCategorias = ("CREATE TABLE $TABLE_CATEGORIAS (" +
                "$COL_C_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_C_NOMBRE TEXT NOT NULL)")
        db.execSQL(createCategorias)

        val createLibros = ("CREATE TABLE $TABLE_LIBROS (" +
                "$COL_L_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_L_TITULO TEXT NOT NULL, " +
                "$COL_L_AUTOR TEXT NOT NULL, " +
                "$COL_L_USUARIO_ID INTEGER, " +
                "$COL_L_CATEGORIA_ID INTEGER, " +
                "$COL_L_FECHA INTEGER, " +
                "$COL_L_VENCIMIENTO INTEGER, " +
                "$COL_L_DEVUELTO INTEGER DEFAULT 0, " +
                "$COL_L_COMENTARIO TEXT, " +
                "FOREIGN KEY($COL_L_USUARIO_ID) REFERENCES $TABLE_USUARIOS($COL_U_ID), " +
                "FOREIGN KEY($COL_L_CATEGORIA_ID) REFERENCES $TABLE_CATEGORIAS($COL_C_ID))")
        db.execSQL(createLibros)

        insertarCategoriaInicial(db, "Matemáticas")
        insertarCategoriaInicial(db, "Programación")
        insertarCategoriaInicial(db, "Contabilidad")
        insertarCategoriaInicial(db, "Educación")
        insertarCategoriaInicial(db, "Historia")

        // Pre-cargar un usuario administrador para pruebas
        val adminId = db.insert(TABLE_USUARIOS, null, ContentValues().apply {
            put(COL_U_NOMBRE, "Administrador")
            put(COL_U_CEDULA, "1234567890")
            put(COL_U_CORREO, "admin")
            put(COL_U_PASSWORD, "1234")
            put(COL_U_INSTITUCION, "U. Central")
            put(COL_U_TELEFONO, "0999999999")
            put(COL_U_DIRECCION, "Av. Quito")
            put(COL_U_ANIO, 2024)
        })

        if (adminId != -1L) {
            val librosMock = listOf("Cálculo Integral", "Álgebra Lineal", "Contabilidad I", "Clean Code")
            librosMock.forEachIndexed { index, titulo ->
                db.insert(TABLE_LIBROS, null, ContentValues().apply {
                    put(COL_L_TITULO, titulo)
                    put(COL_L_AUTOR, "Autor $index")
                    put(COL_L_USUARIO_ID, adminId)
                    put(COL_L_CATEGORIA_ID, (index % 5) + 1)
                    put(COL_L_FECHA, System.currentTimeMillis())
                    put(COL_L_VENCIMIENTO, System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000))
                    put(COL_L_DEVUELTO, 0)
                })
            }
        }
    }

    private fun insertarCategoriaInicial(db: SQLiteDatabase, nombre: String) {
        val values = ContentValues().apply { put(COL_C_NOMBRE, nombre) }
        db.insert(TABLE_CATEGORIAS, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LIBROS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)
    }

    // --- CRUD USUARIO ---

    fun insertarUsuario(usuario: Usuario): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_U_NOMBRE, usuario.nombreCompleto)
            put(COL_U_CEDULA, usuario.cedula)
            put(COL_U_CORREO, usuario.correo)
            put(COL_U_PASSWORD, usuario.password)
            put(COL_U_INSTITUCION, usuario.institucion)
            put(COL_U_TELEFONO, usuario.telefono)
            put(COL_U_DIRECCION, usuario.direccion)
            put(COL_U_ANIO, usuario.anioIngreso)
            put(COL_U_FOTO, usuario.fotoUri)
        }
        return db.insert(TABLE_USUARIOS, null, values)
    }

    fun obtenerUsuarioPorCorreo(correo: String): Usuario? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USUARIOS, null, "$COL_U_CORREO = ?", arrayOf(correo), null, null, null)
        var usuario: Usuario? = null
        if (cursor.moveToFirst()) usuario = cursorToUsuario(cursor)
        cursor.close()
        return usuario
    }

    fun login(correo: String, pass: String): Usuario? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USUARIOS, null, "$COL_U_CORREO = ? AND $COL_U_PASSWORD = ?", arrayOf(correo, pass), null, null, null)
        var usuario: Usuario? = null
        if (cursor.moveToFirst()) usuario = cursorToUsuario(cursor)
        cursor.close()
        return usuario
    }

    fun actualizarUsuario(usuario: Usuario): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_U_NOMBRE, usuario.nombreCompleto)
            put(COL_U_CEDULA, usuario.cedula)
            put(COL_U_FOTO, usuario.fotoUri)
        }
        return db.update(TABLE_USUARIOS, values, "$COL_U_ID = ?", arrayOf(usuario.id.toString()))
    }

    private fun cursorToUsuario(cursor: Cursor): Usuario {
        return Usuario(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_U_ID)),
            nombreCompleto = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_NOMBRE)),
            cedula = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_CEDULA)),
            institucion = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_INSTITUCION)) ?: "",
            telefono = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_TELEFONO)) ?: "",
            direccion = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_DIRECCION)) ?: "",
            anioIngreso = cursor.getInt(cursor.getColumnIndexOrThrow(COL_U_ANIO)),
            correo = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_CORREO)),
            password = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_PASSWORD)),
            fotoUri = cursor.getString(cursor.getColumnIndexOrThrow(COL_U_FOTO))
        )
    }

    // --- CATEGORÍAS ---

    fun obtenerCategorias(): List<Categoria> {
        val lista = mutableListOf<Categoria>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CATEGORIAS", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(Categoria(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_C_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_C_NOMBRE))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    // --- CRUD PRÉSTAMOS ---

    fun insertarPrestamo(libro: Libro): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_L_TITULO, libro.titulo)
            put(COL_L_AUTOR, libro.autor)
            put(COL_L_USUARIO_ID, libro.usuarioId)
            put(COL_L_CATEGORIA_ID, libro.categoriaId)
            put(COL_L_FECHA, libro.fechaPrestamo)
            put(COL_L_VENCIMIENTO, libro.fechaVencimiento)
            put(COL_L_DEVUELTO, if (libro.isDevuelto) 1 else 0)
            put(COL_L_COMENTARIO, libro.comentario)
        }
        return db.insert(TABLE_LIBROS, null, values)
    }

    fun obtenerPrestamosActivos(usuarioId: Int): List<Libro> {
        val lista = mutableListOf<Libro>()
        val db = this.readableDatabase
        val query = "SELECT L.*, C.$COL_C_NOMBRE FROM $TABLE_LIBROS L " +
                    "INNER JOIN $TABLE_CATEGORIAS C ON L.$COL_L_CATEGORIA_ID = C.$COL_C_ID " +
                    "WHERE L.$COL_L_USUARIO_ID = ? AND L.$COL_L_DEVUELTO = 0"
        val cursor = db.rawQuery(query, arrayOf(usuarioId.toString()))
        if (cursor.moveToFirst()) {
            do {
                lista.add(cursorToLibro(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun obtenerHistorialCompleto(usuarioId: Int): List<Libro> {
        val lista = mutableListOf<Libro>()
        val db = this.readableDatabase
        val query = "SELECT L.*, C.$COL_C_NOMBRE FROM $TABLE_LIBROS L " +
                    "INNER JOIN $TABLE_CATEGORIAS C ON L.$COL_L_CATEGORIA_ID = C.$COL_C_ID " +
                    "WHERE L.$COL_L_USUARIO_ID = ? AND L.$COL_L_DEVUELTO = 1"
        val cursor = db.rawQuery(query, arrayOf(usuarioId.toString()))
        if (cursor.moveToFirst()) {
            do {
                lista.add(cursorToLibro(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun actualizarPrestamo(libroId: Int, comentario: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_L_COMENTARIO, comentario)
        }
        return db.update(TABLE_LIBROS, values, "$COL_L_ID = ?", arrayOf(libroId.toString()))
    }

    fun devolverLibro(libroId: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_L_DEVUELTO, 1)
        }
        return db.update(TABLE_LIBROS, values, "$COL_L_ID = ?", arrayOf(libroId.toString()))
    }

    fun eliminarPrestamo(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_LIBROS, "$COL_L_ID = ?", arrayOf(id.toString()))
    }

    private fun cursorToLibro(cursor: Cursor): Libro {
        return Libro(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_L_ID)),
            titulo = cursor.getString(cursor.getColumnIndexOrThrow(COL_L_TITULO)),
            autor = cursor.getString(cursor.getColumnIndexOrThrow(COL_L_AUTOR)),
            usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_L_USUARIO_ID)),
            categoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_L_CATEGORIA_ID)),
            categoriaNombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_C_NOMBRE)),
            fechaPrestamo = cursor.getLong(cursor.getColumnIndexOrThrow(COL_L_FECHA)),
            fechaVencimiento = cursor.getLong(cursor.getColumnIndexOrThrow(COL_L_VENCIMIENTO)),
            isDevuelto = cursor.getInt(cursor.getColumnIndexOrThrow(COL_L_DEVUELTO)) == 1,
            comentario = cursor.getString(cursor.getColumnIndexOrThrow(COL_L_COMENTARIO))
        )
    }
}