package com.example.biblioteca.ui.detalle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.biblioteca.data.Libro
import com.example.biblioteca.data.SessionManager
import com.example.biblioteca.data.local.AppDatabase
import com.example.biblioteca.data.remote.RetrofitClient
import com.example.biblioteca.data.remote.WorkDetailResponse
import kotlinx.coroutines.launch

class DetalleViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val sessionManager = SessionManager(application)

    private val _detalle = MutableLiveData<WorkDetailResponse>()
    val detalle: LiveData<WorkDetailResponse> = _detalle

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _prestamoExitoso = MutableLiveData<Boolean>()
    val prestamoExitoso: LiveData<Boolean> = _prestamoExitoso

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarDetalle(workId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.instance.getWorkDetail(workId)
                _detalle.value = response
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al cargar el detalle: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun solicitarPrestamo(titulo: String, autor: String) {
        val email = sessionManager.getUserEmail() ?: return
        
        viewModelScope.launch {
            try {
                val usuario = db.usuarioDao().buscarPorCorreo(email)
                if (usuario != null) {
                    val yaPrestado = db.libroDao().buscarPrestamo(titulo, usuario.id)
                    if (yaPrestado == null) {
                        val nuevoLibro = Libro(
                            titulo = titulo,
                            autor = autor,
                            usuarioId = usuario.id
                        )
                        db.libroDao().prestamo(nuevoLibro)
                        _prestamoExitoso.value = true
                    } else {
                        _error.value = "Ya tienes este libro prestado"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error en préstamo: ${e.message}"
            }
        }
    }
}