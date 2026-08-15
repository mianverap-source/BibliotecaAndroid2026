package com.example.biblioteca.ui.perfil

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.data.Libro

class MisPrestamosFragment : Fragment(R.layout.fragment_mis_prestamos) {

    private val viewModel: MisPrestamosViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvPrestamos = view.findViewById<RecyclerView>(R.id.rvMisPrestamos)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmptyPrestamos)

        val adapter = PrestamoAdapter(
            libros = emptyList(),
            onDevolverClick = { libro -> mostrarConfirmacionDevolucion(libro) },
            onEditarClick = { libro -> mostrarDialogoComentario(libro) }
        )
        rvPrestamos.layoutManager = LinearLayoutManager(requireContext())
        rvPrestamos.adapter = adapter

        viewModel.librosPrestados.observe(viewLifecycleOwner) { libros ->
            adapter.updateData(libros)
            tvEmpty.visibility = if (libros.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.cargarPrestamos()
    }

    private fun mostrarConfirmacionDevolucion(libro: Libro) {
        AlertDialog.Builder(requireContext())
            .setTitle("Devolver Libro")
            .setMessage("¿Confirmas que has regresado el libro '${libro.titulo}' a la biblioteca física?")
            .setPositiveButton("Confirmar Devolución") { _, _ ->
                viewModel.devolverLibro(libro.id)
                Toast.makeText(requireContext(), "Libro devuelto exitosamente", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Aún no", null)
            .show()
    }

    private fun mostrarDialogoComentario(libro: Libro) {
        val input = EditText(requireContext())
        input.hint = "Escribe un comentario o motivo de prórroga..."
        input.setText(libro.comentario ?: "")

        AlertDialog.Builder(requireContext())
            .setTitle("Actualizar Préstamo")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val comentario = input.text.toString().trim()
                viewModel.actualizarComentario(libro.id, comentario)
                Toast.makeText(requireContext(), "Comentario actualizado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}