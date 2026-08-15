package com.example.biblioteca.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "biblioteca_universitaria.db"
        private const val DATABASE_VERSION = 1

        // Tablas
        const val TABLE_USUARIOS = "usuarios"
        const val TABLE_CATEGORIAS = "categorias"
        const val TABLE_LIBROS = "libros"

        // Columnas Usuarios
        const val COL_U_ID = "id"
        const val COL_U_NOMBRE = "nombreCompleto"
        const val COL_U_CEDULA = "cedula"
        const val COL_U_CORREO = "correo"
        const val COL_U_PASSWORD = "password"
        const val COL_U_FOTO = "fotoUri"

        // Columnas Categorías
        const val COL_C_ID = "id"
        const val COL_C_NOMBRE = "nombre"

        // Columnas Libros (Préstamos)
        const val COL_L_ID = "id"
        const val COL_L_TITULO = "titulo"
        const val COL_L_AUTOR = "autor"
        const val COL_L_USUARIO_ID = "usuarioId"
        const val COL_L_CATEGORIA_ID = "categoriaId"
        const val COL_L_FECHA = "fechaPrestamo"
        const val COL_L_DEVUELTO = "isDevuelto"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear Tabla Usuarios
        val createUsuarios = ("CREATE TABLE $TABLE_USUARIOS (" +
                "$COL_U_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_U_NOMBRE TEXT NOT NULL, " +
                "$COL_U_CEDULA TEXT UNIQUE NOT NULL, " +
                "$COL_U_CORREO TEXT UNIQUE NOT NULL, " +
                "$COL_U_PASSWORD TEXT NOT NULL, " +
                "$COL_U_FOTO TEXT)")
        db.execSQL(createUsuarios)

        // Crear Tabla Categorías
        val createCategorias = ("CREATE TABLE $TABLE_CATEGORIAS (" +
                "$COL_C_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_C_NOMBRE TEXT NOT NULL)")
        db.execSQL(createCategorias)

        // Crear Tabla Libros con Foreign Keys
        val createLibros = ("CREATE TABLE $TABLE_LIBROS (" +
                "$COL_L_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_L_TITULO TEXT NOT NULL, " +
                "$COL_L_AUTOR TEXT NOT NULL, " +
                "$COL_L_USUARIO_ID INTEGER, " +
                "$COL_L_CATEGORIA_ID INTEGER, " +
                "$COL_L_FECHA INTEGER, " +
                "$COL_L_DEVUELTO INTEGER DEFAULT 0, " +
                "FOREIGN KEY($COL_L_USUARIO_ID) REFERENCES $TABLE_USUARIOS($COL_U_ID), " +
                "FOREIGN KEY($COL_L_CATEGORIA_ID) REFERENCES $TABLE_CATEGORIAS($COL_C_ID))")
        db.execSQL(createLibros)

        // Insertar categorías iniciales (Requerimiento Avance 2)
        insertarCategoriaInicial(db, "Matemáticas")
        insertarCategoriaInicial(db, "Programación")
        insertarCategoriaInicial(db, "Contabilidad")
        insertarCategoriaInicial(db, "Educación")
        insertarCategoriaInicial(db, "Historia")
    }

    private fun insertarCategoriaInicial(db: SQLiteDatabase, nombre: String) {
        val values = ContentValues().apply {
            put(COL_C_NOMBRE, nombre)
        }
        db.insert(TABLE_CATEGORIAS, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LIBROS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)
    }
}