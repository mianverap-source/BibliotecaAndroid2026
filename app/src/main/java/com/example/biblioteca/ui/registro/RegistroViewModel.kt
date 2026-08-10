package com.example.biblioteca.ui.registro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.biblioteca.data.local.AppDatabase
import com.example.biblioteca.data.local.entities.Usuario
import kotlinx.coroutines.launch

class RegistroViewModel(application: Application) : AndroidViewModel(application) {

    private val usuarioDao = AppDatabase.getDatabase(application).usuarioDao()

    private val _registroExitoso = MutableLiveData<Boolean>()
    val registroExitoso: LiveData<Boolean> = _registroExitoso

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun registrar(
        nombre: String,
        cedula: String,
        institucion: String,
        celular: String,
        direccion: String,
        anio: String,
        correo: String,
        password: String,
        confirm: String
    ) {
        if (nombre.isEmpty() || cedula.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            _error.value = "Completa los campos obligatorios"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            _error.value = "Correo electrónico no válido"
            return
        }

        if (cedula.length != 10) {
            _error.value = "La cédula debe tener 10 dígitos"
            return
        }

        if (password.length < 6) {
            _error.value = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        if (password != confirm) {
            _error.value = "Las contraseñas no coinciden"
            return
        }

        val anioInt = anio.toIntOrNull() ?: 0

        viewModelScope.launch {
            try {
                val existe = usuarioDao.buscarPorCorreoOCedula(correo, cedula)
                if (existe != null) {
                    _error.value = "El correo o la cédula ya están registrados"
                } else {
                    val nuevoUsuario = Usuario(
                        nombreCompleto = nombre,
                        cedula = cedula,
                        institucion = institucion,
                        telefono = celular,
                        direccion = direccion,
                        anioIngreso = anioInt,
                        correo = correo,
                        password = password
                    )
                    usuarioDao.registrar(nuevoUsuario)
                    _registroExitoso.value = true
                }
            } catch (e: Exception) {
                _error.value = "Error al registrar: ${e.message}"
            }
        }
    }

    fun errorMostrado() {
        _error.value = null
    }
}