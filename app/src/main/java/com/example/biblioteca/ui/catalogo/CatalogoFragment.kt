package com.example.biblioteca.ui.catalogo

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.data.local.entities.Categoria

class CatalogoFragment : Fragment(R.layout.fragment_catalogo) {

    private val viewModel: CatalogoViewModel by viewModels()
    private lateinit var adapter: LibroAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvLibros = view.findViewById<RecyclerView>(R.id.rvLibros)
        val progressBar = view.findViewById<ProgressBar>(R.id.pbLoading)
        val searchView = view.findViewById<SearchView>(R.id.svLibros)
        val tvEmpty = view.findViewById<android.widget.TextView>(R.id.tvEmptyState)
        val spCategories = view.findViewById<Spinner>(R.id.spCatalogoCategorias)

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

        viewModel.categorias.observe(viewLifecycleOwner) { cats ->
            val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cats)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spCategories.adapter = spinnerAdapter
        }

        spCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val cat = parent?.getItemAtPosition(position) as? Categoria
                cat?.let {
                    searchView.setQuery("", false)
                    viewModel.buscarLibros(it.nombre.lowercase())
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        adapter = LibroAdapter(emptyList()) { libroSeleccionado ->
            val bundle = Bundle().apply {
                putString("workId", libroSeleccionado.getWorkId())
                putString("titulo", libroSeleccionado.title)
                putString("autor", libroSeleccionado.authorName?.firstOrNull())
                putString("coverUrl", libroSeleccionado.getCoverUrl())
                // Pasar la categoría actual
                val currentCat = spCategories.selectedItem as? Categoria
                currentCat?.let {
                    putInt("categoriaId", it.id)
                    putString("categoriaNombre", it.nombre)
                }
            }
            findNavController().navigate(R.id.action_catalogo_to_detalle, bundle)
        }

        rvLibros.layoutManager = LinearLayoutManager(requireContext())
        rvLibros.adapter = adapter

        viewModel.libros.observe(viewLifecycleOwner) { libros ->
            adapter.updateData(libros)
            tvEmpty.visibility = if (libros.isEmpty() && viewModel.isLoading.value == false) View.VISIBLE else View.GONE
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

        viewModel.cargarCategorias()
        viewModel.buscarLibros()
    }
}