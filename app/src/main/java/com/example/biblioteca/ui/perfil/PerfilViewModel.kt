package com.example.biblioteca.ui.perfil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.biblioteca.data.Libro
import com.example.biblioteca.data.SessionManager
import com.example.biblioteca.data.local.AppDatabase
import com.example.biblioteca.data.local.entities.Usuario
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val usuarioDao = db.usuarioDao()
    private val sessionManager = SessionManager(application)

    private val _usuario = MutableLiveData<Usuario?>()
    val usuario: LiveData<Usuario?> = _usuario

    private val _librosPrestados = MutableLiveData<List<Libro>>()
    val librosPrestados: LiveData<List<Libro>> = _librosPrestados

    fun cargarDatosUsuario() {
        val email = sessionManager.getUserEmail()
        if (email != null) {
            viewModelScope.launch {
                val user = usuarioDao.buscarPorCorreo(email)
                _usuario.value = user
                
                if (user != null) {
                    val libros = db.libroDao().getLibrosPorUsuario(user.id)
                    _librosPrestados.value = libros
                }
            }
        }
    }

    fun cerrarSesion() {
        sessionManager.clearSession()
    }
}