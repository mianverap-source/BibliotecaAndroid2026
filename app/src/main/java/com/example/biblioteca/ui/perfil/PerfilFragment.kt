package com.example.biblioteca.ui.perfil

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R

class PerfilFragment : Fragment(R.layout.fragment_perfil) {

    private val viewModel: PerfilViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvNombre = view.findViewById<TextView>(R.id.tvNombrePerfil)
        val tvCorreo = view.findViewById<TextView>(R.id.tvCorreoPerfil)
        val tvCedula = view.findViewById<TextView>(R.id.tvCedulaPerfil)
        val tvInstitucion = view.findViewById<TextView>(R.id.tvInstitucionPerfil)
        val tvTelefono = view.findViewById<TextView>(R.id.tvTelefonoPerfil)
        val tvDireccion = view.findViewById<TextView>(R.id.tvDireccionPerfil)
        val tvAnio = view.findViewById<TextView>(R.id.tvAnioPerfil)
        val btnCerrarSesion = view.findViewById<View>(R.id.btnCerrarSesion)
        val rvPrestamos = view.findViewById<RecyclerView>(R.id.rvMisPrestamos)

        val adapter = PrestamoAdapter(emptyList())
        rvPrestamos.layoutManager = LinearLayoutManager(requireContext())
        rvPrestamos.adapter = adapter

        viewModel.usuario.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                tvNombre.text = user.nombreCompleto
                tvCorreo.text = user.correo
                tvCedula.text = "Cédula: ${user.cedula}"
                tvInstitucion.text = "Institución: ${user.institucion}"
                tvTelefono.text = "Celular: ${user.telefono}"
                tvDireccion.text = "Dirección: ${user.direccion}"
                tvAnio.text = "Año de ingreso: ${user.anioIngreso}"
            }
        }

        viewModel.librosPrestados.observe(viewLifecycleOwner) { libros ->
            adapter.updateData(libros)
        }

        btnCerrarSesion.setOnClickListener {
            viewModel.cerrarSesion()
            findNavController().navigate(R.id.action_perfil_to_login)
        }

        viewModel.cargarDatosUsuario()
    }
}