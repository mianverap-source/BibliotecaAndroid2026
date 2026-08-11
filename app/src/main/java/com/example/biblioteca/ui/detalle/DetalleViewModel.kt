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
import com.example.biblioteca.data.remote.SimulatedLibraryInfo
import com.example.biblioteca.data.remote.WorkDetailResponse
import kotlinx.coroutines.launch
import kotlin.random.Random

class DetalleViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val sessionManager = SessionManager(application)

    private val _detalle = MutableLiveData<WorkDetailResponse>()
    val detalle: LiveData<WorkDetailResponse> = _detalle

    private val _simulatedInfo = MutableLiveData<SimulatedLibraryInfo>()
    val simulatedInfo: LiveData<SimulatedLibraryInfo> = _simulatedInfo

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
                _simulatedInfo.value = generateSimulatedInfo(workId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al cargar el detalle: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun generateSimulatedInfo(workId: String): SimulatedLibraryInfo {
        // Determinismo basado en el ID para que no cambie al rotar pantalla
        val seed = workId.hashCode().toLong()
        val random = Random(seed)
        
        val esDigital = random.nextBoolean()
        return if (esDigital) {
            SimulatedLibraryInfo(esDigital = true)
        } else {
            val pisos = listOf("Piso 1", "Piso 2", "Piso 3")
            val modulos = listOf("Módulo A", "Módulo B", "Módulo C", "Módulo D")
            val estantes = (1..10).map { "Estante $it" }
            
            val totales = random.nextInt(3, 10)
            val disponibles = random.nextInt(0, totales + 1)
            
            SimulatedLibraryInfo(
                esDigital = false,
                ubicacion = "${pisos.random(random)}, ${modulos.random(random)}, ${estantes.random(random)}",
                copiasTotales = totales,
                copiasDisponibles = disponibles
            )
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