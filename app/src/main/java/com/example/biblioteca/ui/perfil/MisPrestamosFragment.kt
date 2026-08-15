package com.example.biblioteca.ui.perfil

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.data.Libro
import com.example.biblioteca.data.local.entities.Categoria

class MisPrestamosFragment : Fragment(R.layout.fragment_mis_prestamos) {

    private val viewModel: MisPrestamosViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvPrestamos = view.findViewById<RecyclerView>(R.id.rvMisPrestamos)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmptyPrestamos)

        val adapter = PrestamoAdapter(
            libros = emptyList(),
            onDevolverClick = { libro -> viewModel.devolverLibro(libro.id) },
            onEditarClick = { libro -> mostrarDialogoEdicion(libro) },
            onEliminarClick = { libro -> mostrarConfirmacionEliminar(libro) }
        )
        rvPrestamos.layoutManager = LinearLayoutManager(requireContext())
        rvPrestamos.adapter = adapter

        viewModel.librosPrestados.observe(viewLifecycleOwner) { libros ->
            adapter.updateData(libros)
            tvEmpty.visibility = if (libros.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.cargarPrestamos()
    }

    private fun mostrarConfirmacionEliminar(libro: Libro) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este registro de préstamo?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.eliminarPrestamo(libro.id)
                Toast.makeText(requireContext(), "Registro eliminado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEdicion(libro: Libro) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_prestamo, null)
        val spinner = dialogView.findViewById<Spinner>(R.id.spEditCategoria)
        
        viewModel.categorias.value?.let { cats ->
            val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cats)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = spinnerAdapter
            
            val currentPos = cats.indexOfFirst { it.id == libro.categoriaId }
            if (currentPos != -1) spinner.setSelection(currentPos)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Editar Categoría")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val selectedCat = spinner.selectedItem as? Categoria
                if (selectedCat != null) {
                    viewModel.editarPrestamo(libro.id, selectedCat.id)
                    Toast.makeText(requireContext(), "Cambios guardados", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}