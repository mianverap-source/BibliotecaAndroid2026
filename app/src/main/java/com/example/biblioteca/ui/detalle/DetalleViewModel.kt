package com.example.biblioteca.ui.detalle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.biblioteca.data.Libro
import com.example.biblioteca.data.SessionManager
import com.example.biblioteca.data.local.DatabaseHelper
import com.example.biblioteca.data.local.entities.Categoria
import com.example.biblioteca.data.remote.RetrofitClient
import com.example.biblioteca.data.remote.SimulatedLibraryInfo
import com.example.biblioteca.data.remote.WorkDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class DetalleViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = DatabaseHelper(application)
    private val sessionManager = SessionManager(application)

    private val _detalle = MutableLiveData<WorkDetailResponse>()
    val detalle: LiveData<WorkDetailResponse> = _detalle

    private val _simulatedInfo = MutableLiveData<SimulatedLibraryInfo>()
    val simulatedInfo: LiveData<SimulatedLibraryInfo> = _simulatedInfo

    private val _categorias = MutableLiveData<List<Categoria>>()
    val categorias: LiveData<List<Categoria>> = _categorias

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
                
                // Cargar categorías de la base de datos
                val cats = withContext(Dispatchers.IO) {
                    dbHelper.obtenerCategorias()
                }
                _categorias.value = cats
                
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al cargar el detalle: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun generateSimulatedInfo(workId: String): SimulatedLibraryInfo {
        val seed = workId.hashCode().toLong()
        val random = Random(seed)
        val esDigital = random.nextBoolean()
        return if (esDigital) {
            SimulatedLibraryInfo(esDigital = true)
        } else {
            val pisos = listOf("Piso 1", "Piso 2", "Piso 3")
            val modulos = listOf("Módulo A", "Módulo B", "Módulo C")
            SimulatedLibraryInfo(
                esDigital = false,
                ubicacion = "${pisos.random(random)}, ${modulos.random(random)}",
                copiasTotales = random.nextInt(3, 8),
                copiasDisponibles = random.nextInt(1, 4)
            )
        }
    }

    fun solicitarPrestamo(titulo: String, autor: String, categoriaId: Int) {
        val email = sessionManager.getUserEmail() ?: return
        
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val usuario = dbHelper.obtenerUsuarioPorCorreo(email)
                    if (usuario != null) {
                        val nuevoLibro = Libro(
                            titulo = titulo,
                            autor = autor,
                            usuarioId = usuario.id,
                            categoriaId = categoriaId,
                            fechaPrestamo = System.currentTimeMillis(),
                            isDevuelto = false
                        )
                        dbHelper.insertarPrestamo(nuevoLibro)
                    } else {
                        -1L
                    }
                }
                
                if (result != -1L) {
                    _prestamoExitoso.value = true
                } else {
                    _error.value = "Error al procesar el préstamo"
                }
            } catch (e: Exception) {
                _error.value = "Error en préstamo: ${e.message}"
            }
        }
    }
}