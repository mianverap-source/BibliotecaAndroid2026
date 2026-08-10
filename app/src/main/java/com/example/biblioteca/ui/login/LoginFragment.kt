package com.example.biblioteca.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.biblioteca.R
import com.google.android.material.textfield.TextInputEditText

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etUsuario = view.findViewById<TextInputEditText>(R.id.etUsuario)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etPassword)
        val tvError = view.findViewById<android.widget.TextView>(R.id.tvError)
        val btnIngresar = view.findViewById<View>(R.id.btnIngresar)
        val tvIrRegistro = view.findViewById<View>(R.id.tvIrRegistro)

        // Observar resultado del login
        viewModel.loginResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigate(R.id.action_login_to_catalogo)
            }
        }

        // Observar errores
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                tvError.text = error
                tvError.visibility = View.VISIBLE
                viewModel.errorMostrado()
            }
        }

        btnIngresar.setOnClickListener {
            val usuario = etUsuario.text?.toString()?.trim() ?: ""
            val password = etPassword.text?.toString()?.trim() ?: ""
            viewModel.iniciarSesion(usuario, password)
        }

        tvIrRegistro.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_registro)
        }
    }
}