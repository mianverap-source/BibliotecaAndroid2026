package com.example.biblioteca.ui.perfil

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.biblioteca.R
import com.example.biblioteca.data.Libro
import com.example.biblioteca.data.local.entities.Categoria

class PerfilFragment : Fragment(R.layout.fragment_perfil) {

    private val viewModel: PerfilViewModel by viewModels()

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            try {
                requireContext().contentResolver.takePersistableUriPermission(
                    it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {}
            viewModel.actualizarFoto(it.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivFoto = view.findViewById<ImageView>(R.id.ivPerfilFoto)
        val fabFoto = view.findViewById<View>(R.id.fabCambiarFoto)
        val tvNombre = view.findViewById<TextView>(R.id.tvNombrePerfil)
        val tvCorreo = view.findViewById<TextView>(R.id.tvCorreoPerfil)
        val tvCedula = view.findViewById<TextView>(R.id.tvCedulaPerfil)
        val tvInstitucion = view.findViewById<TextView>(R.id.tvInstitucionPerfil)
        val tvTelefono = view.findViewById<TextView>(R.id.tvTelefonoPerfil)
        val tvDireccion = view.findViewById<TextView>(R.id.tvDireccionPerfil)
        val tvAnio = view.findViewById<TextView>(R.id.tvAnioPerfil)
        val btnCerrarSesion = view.findViewById<View>(R.id.btnCerrarSesion)

        fabFoto.setOnClickListener {
            pickImage.launch("image/*")
        }

        viewModel.usuario.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                tvNombre.text = user.nombreCompleto
                tvCorreo.text = user.correo
                tvCedula.text = "Cédula: ${user.cedula}"
                tvInstitucion.text = "Institución: ${user.institucion}"
                tvTelefono.text = "Celular: ${user.telefono}"
                tvDireccion.text = "Dirección: ${user.direccion}"
                tvAnio.text = "Año de ingreso: ${user.anioIngreso}"
                if (user.fotoUri != null) {
                    ivFoto.load(user.fotoUri) {
                        transformations(CircleCropTransformation())
                    }
                }
            }
        }

        btnCerrarSesion.setOnClickListener {
            viewModel.cerrarSesion()
            findNavController().navigate(R.id.action_perfil_to_login)
        }

        viewModel.cargarDatosUsuario()
    }
}