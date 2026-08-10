package com.example.biblioteca.ui.detalle

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.biblioteca.R

class DetalleLibroFragment : Fragment(R.layout.fragment_detalle_libro) {

    private val viewModel: DetalleViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivPortada = view.findViewById<ImageView>(R.id.ivDetallePortada)
        val tvTitulo = view.findViewById<TextView>(R.id.tvDetalleTitulo)
        val tvAutor = view.findViewById<TextView>(R.id.tvDetalleAutor)
        val tvDescripcion = view.findViewById<TextView>(R.id.tvDetalleDescripcion)
        val progressBar = view.findViewById<ProgressBar>(R.id.pbDetalleLoading)
        val btnPrestamo = view.findViewById<android.widget.Button>(R.id.btnSolicitarPrestamo)

        // Obtener datos básicos del Bundle (pasados desde el catálogo)
        val workId = arguments?.getString("workId") ?: ""
        val titulo = arguments?.getString("titulo") ?: ""
        val autor = arguments?.getString("autor") ?: ""
        val coverUrl = arguments?.getString("coverUrl") ?: ""

        tvTitulo.text = titulo
        tvAutor.text = autor
        ivPortada.load(coverUrl) {
            placeholder(android.R.drawable.ic_menu_gallery)
            error(android.R.drawable.stat_notify_error)
        }

        btnPrestamo.setOnClickListener {
            viewModel.solicitarPrestamo(titulo, autor)
        }

        viewModel.detalle.observe(viewLifecycleOwner) { detalle ->
            tvDescripcion.text = detalle.getDescriptionText()
        }

        viewModel.prestamoExitoso.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Libro prestado con éxito", Toast.LENGTH_SHORT).show()
                btnPrestamo.isEnabled = false
                btnPrestamo.text = "Prestado"
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                tvDescripcion.text = "No se pudo cargar la descripción."
            }
        }

        if (workId.isNotEmpty()) {
            viewModel.cargarDetalle(workId)
        }
    }
}