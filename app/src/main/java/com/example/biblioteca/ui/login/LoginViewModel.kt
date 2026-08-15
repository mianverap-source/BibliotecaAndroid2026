package com.example.biblioteca.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.biblioteca.data.SessionManager
import com.example.biblioteca.data.local.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = DatabaseHelper(application)
    private val sessionManager = SessionManager(application)

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun iniciarSesion(usuario: String, password: String) {
        if (usuario.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Completa usuario y contraseña"
            return
        }

        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                dbHelper.login(usuario, password)
            }
            if (user != null) {
                sessionManager.saveUserEmail(user.correo)
                _loginResult.value = true
            } else {
                _errorMessage.value = "Usuario o contraseña incorrectos"
            }
        }
    }

    fun errorMostrado() {
        _errorMessage.value = null
    }
}