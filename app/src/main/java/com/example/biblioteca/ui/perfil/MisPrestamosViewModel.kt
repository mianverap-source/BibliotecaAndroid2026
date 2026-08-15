package com.example.biblioteca.ui.perfil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.biblioteca.data.Libro
import com.example.biblioteca.data.SessionManager
import com.example.biblioteca.data.local.DatabaseHelper
import com.example.biblioteca.data.local.entities.Categoria
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MisPrestamosViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = DatabaseHelper(application)
    private val sessionManager = SessionManager(application)

    private val _librosPrestados = MutableLiveData<List<Libro>>()
    val librosPrestados: LiveData<List<Libro>> = _librosPrestados

    private val _categorias = MutableLiveData<List<Categoria>>()
    val categorias: LiveData<List<Categoria>> = _categorias

    fun cargarPrestamos() {
        val email = sessionManager.getUserEmail() ?: return
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                dbHelper.obtenerUsuarioPorCorreo(email)
            }
            if (user != null) {
                val libros = withContext(Dispatchers.IO) {
                    dbHelper.obtenerPrestamosActivos(user.id)
                }
                _librosPrestados.value = libros
            }

            val cats = withContext(Dispatchers.IO) {
                dbHelper.obtenerCategorias()
            }
            _categorias.value = cats
        }
    }

    fun devolverLibro(libroId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbHelper.devolverLibro(libroId)
            }
            cargarPrestamos()
        }
    }

    fun editarPrestamo(libroId: Int, categoriaId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbHelper.actualizarPrestamo(libroId, categoriaId)
            }
            cargarPrestamos()
        }
    }

    fun eliminarPrestamo(libroId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbHelper.eliminarPrestamo(libroId)
            }
            cargarPrestamos()
        }
    }
}