package com.example.biblioteca.ui.catalogo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.biblioteca.data.local.DatabaseHelper
import com.example.biblioteca.data.local.entities.Categoria
import com.example.biblioteca.data.remote.BookDoc
import com.example.biblioteca.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatalogoViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = DatabaseHelper(application)

    private val _libros = MutableLiveData<List<BookDoc>>()
    val libros: LiveData<List<BookDoc>> = _libros

    private val _categorias = MutableLiveData<List<Categoria>>()
    val categorias: LiveData<List<Categoria>> = _categorias

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarCategorias() {
        viewModelScope.launch {
            val cats = withContext(Dispatchers.IO) {
                dbHelper.obtenerCategorias()
            }
            _categorias.value = cats
        }
    }

    fun buscarLibros(query: String = "android") {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.instance.searchBooks(query)
                if (response.docs.isEmpty()) {
                    _error.value = "No se encontraron libros para: $query"
                    _libros.value = emptyList()
                } else {
                    _libros.value = response.docs
                    _error.value = null
                }
            } catch (e: java.net.UnknownHostException) {
                _error.value = "Sin conexión a internet"
            } catch (e: Exception) {
                _error.value = "Error al cargar libros: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}