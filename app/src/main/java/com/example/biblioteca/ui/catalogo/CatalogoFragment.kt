package com.example.biblioteca.ui.catalogo

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R

class CatalogoFragment : Fragment(R.layout.fragment_catalogo) {

    private val viewModel: CatalogoViewModel by viewModels()
    private lateinit var adapter: LibroAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvLibros = view.findViewById<RecyclerView>(R.id.rvLibros)
        val btnVerPerfil = view.findViewById<View>(R.id.btnVerPerfil)
        val progressBar = view.findViewById<ProgressBar>(R.id.pbLoading)
        val searchView = view.findViewById<SearchView>(R.id.svLibros)
        val tvEmpty = view.findViewById<android.widget.TextView>(R.id.tvEmptyState)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    tvEmpty.visibility = View.GONE
                    viewModel.buscarLibros(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        adapter = LibroAdapter(emptyList()) { libroSeleccionado ->
            val bundle = Bundle().apply {
                putString("workId", libroSeleccionado.getWorkId())
                putString("titulo", libroSeleccionado.title)
                putString("autor", libroSeleccionado.authorName?.firstOrNull())
                putString("coverUrl", libroSeleccionado.getCoverUrl())
            }
            findNavController().navigate(R.id.action_catalogo_to_detalle, bundle)
        }

        rvLibros.layoutManager = LinearLayoutManager(requireContext())
        rvLibros.adapter = adapter

        btnVerPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_catalogo_to_perfil)
        }

        viewModel.libros.observe(viewLifecycleOwner) { libros ->
            adapter.updateData(libros)
            if (libros.isEmpty() && viewModel.isLoading.value == false) {
                tvEmpty.visibility = View.VISIBLE
            } else {
                tvEmpty.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) tvEmpty.visibility = View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                tvEmpty.text = it
                tvEmpty.visibility = View.VISIBLE
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.buscarLibros()
    }
}