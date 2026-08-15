package com.example.biblioteca.ui.perfil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.biblioteca.data.SessionManager
import com.example.biblioteca.data.local.DatabaseHelper
import com.example.biblioteca.data.local.entities.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = DatabaseHelper(application)
    private val sessionManager = SessionManager(application)

    private val _usuario = MutableLiveData<Usuario?>()
    val usuario: LiveData<Usuario?> = _usuario

    fun cargarDatosUsuario() {
        val email = sessionManager.getUserEmail()
        if (email != null) {
            viewModelScope.launch {
                val user = withContext(Dispatchers.IO) {
                    dbHelper.obtenerUsuarioPorCorreo(email)
                }
                _usuario.value = user
            }
        }
    }

    fun cerrarSesion() {
        sessionManager.clearSession()
    }

    fun actualizarFoto(uri: String) {
        val email = sessionManager.getUserEmail() ?: return
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                dbHelper.obtenerUsuarioPorCorreo(email)
            }
            if (user != null) {
                val userActualizado = user.copy(fotoUri = uri)
                withContext(Dispatchers.IO) {
                    dbHelper.actualizarUsuario(userActualizado)
                }
                _usuario.value = userActualizado
            }
        }
    }
}