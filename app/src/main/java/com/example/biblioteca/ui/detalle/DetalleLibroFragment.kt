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
        val btnPrestamo = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSolicitarPrestamo)
        
        val ivTipoIcon = view.findViewById<ImageView>(R.id.ivTipoIcon)
        val tvTipoRecurso = view.findViewById<TextView>(R.id.tvTipoRecurso)
        val tvUbicacion = view.findViewById<TextView>(R.id.tvDetalleUbicacion)
        val tvCopias = view.findViewById<TextView>(R.id.tvDetalleCopias)

        // Obtener datos básicos del Bundle
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

        viewModel.simulatedInfo.observe(viewLifecycleOwner) { info ->
            if (info.esDigital) {
                ivTipoIcon.setImageResource(android.R.drawable.ic_menu_save)
                tvTipoRecurso.text = "Recurso Digital"
                tvUbicacion.text = "Disponible para descarga inmediata"
                tvCopias.visibility = View.GONE
                btnPrestamo.text = "Descargar PDF"
                btnPrestamo.setIconResource(android.R.drawable.ic_menu_save)
            } else {
                ivTipoIcon.setImageResource(android.R.drawable.ic_menu_agenda)
                tvTipoRecurso.text = "Libro Físico"
                tvUbicacion.text = "Ubicación: ${info.ubicacion}"
                tvCopias.text = "Disponibilidad: ${info.copiasDisponibles} de ${info.copiasTotales} copias"
                tvCopias.visibility = View.VISIBLE
                btnPrestamo.text = "Solicitar Préstamo Físico"
                btnPrestamo.setIconResource(android.R.drawable.ic_input_add)
                
                if (info.copiasDisponibles == 0) {
                    btnPrestamo.isEnabled = false
                    btnPrestamo.text = "Sin copias disponibles"
                }
            }
        }

        btnPrestamo.setOnClickListener {
            viewModel.simulatedInfo.value?.let { info ->
                if (info.esDigital) {
                    Toast.makeText(requireContext(), "Iniciando descarga de: $titulo...", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.solicitarPrestamo(titulo, autor)
                }
            }
        }

        viewModel.detalle.observe(viewLifecycleOwner) { detalle ->
            tvDescripcion.text = detalle.getDescriptionText()
        }

        viewModel.prestamoExitoso.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Libro reservado con éxito", Toast.LENGTH_SHORT).show()
                btnPrestamo.isEnabled = false
                btnPrestamo.text = "Reservado (Retirar en ventanilla)"
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