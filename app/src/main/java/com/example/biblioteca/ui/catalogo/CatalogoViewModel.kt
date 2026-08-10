package com.example.biblioteca.ui.catalogo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biblioteca.data.remote.BookDoc
import com.example.biblioteca.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class CatalogoViewModel : ViewModel() {

    private val _libros = MutableLiveData<List<BookDoc>>()
    val libros: LiveData<List<BookDoc>> = _libros

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

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