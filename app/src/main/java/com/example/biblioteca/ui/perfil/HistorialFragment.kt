package com.example.biblioteca.ui.perfil

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R

class HistorialFragment : Fragment(R.layout.fragment_historial) {

    private val viewModel: MisPrestamosViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvHistorial = view.findViewById<RecyclerView>(R.id.rvHistorial)

        val adapter = PrestamoAdapter(
            libros = emptyList(),
            onDevolverClick = {},
            onEditarClick = {},
            isHistorial = true
        )
        rvHistorial.layoutManager = LinearLayoutManager(requireContext())
        rvHistorial.adapter = adapter

        viewModel.historial.observe(viewLifecycleOwner) { libros ->
            adapter.updateData(libros)
        }

        viewModel.cargarPrestamos()
    }
}