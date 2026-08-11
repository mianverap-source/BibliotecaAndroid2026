package com.example.biblioteca.ui.registro

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.biblioteca.R
import com.google.android.material.textfield.TextInputEditText

class RegistroFragment : Fragment(R.layout.fragment_registro) {

    private val viewModel: RegistroViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etNombre = view.findViewById<TextInputEditText>(R.id.etNombre)
        val etCedula = view.findViewById<TextInputEditText>(R.id.etCedula)
        val etInstitucion = view.findViewById<TextInputEditText>(R.id.etInstitucion)
        val etCelular = view.findViewById<TextInputEditText>(R.id.etCelular)
        val etDireccion = view.findViewById<TextInputEditText>(R.id.etDireccion)
        val etAnio = view.findViewById<TextInputEditText>(R.id.etAnioIngreso)
        val etCorreo = view.findViewById<TextInputEditText>(R.id.etCorreo)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etPasswordReg)
        val etConfirm = view.findViewById<TextInputEditText>(R.id.etPasswordConfirm)
        val tvError = view.findViewById<TextView>(R.id.tvErrorRegistro)
        val btnRegistrar = view.findViewById<View>(R.id.btnRegistrar)
        val tvVolverLogin = view.findViewById<View>(R.id.tvVolverLogin)

        viewModel.registroExitoso.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null) {
                tvError.text = errorMsg
                tvError.visibility = View.VISIBLE
                viewModel.errorMostrado()
            }
        }

        btnRegistrar.setOnClickListener {
            viewModel.registrar(
                etNombre.text.toString().trim(),
                etCedula.text.toString().trim(),
                etInstitucion.text.toString().trim(),
                etCelular.text.toString().trim(),
                etDireccion.text.toString().trim(),
                etAnio.text.toString().trim(),
                etCorreo.text.toString().trim(),
                etPassword.text.toString().trim(),
                etConfirm.text.toString().trim()
            )
        }

        tvVolverLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}