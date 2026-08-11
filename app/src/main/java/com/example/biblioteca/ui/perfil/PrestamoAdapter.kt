package com.example.biblioteca.ui.perfil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.data.Libro

class PrestamoAdapter(
    private var libros: List<Libro>,
    private val onDevolverClick: (Libro) -> Unit
) : RecyclerView.Adapter<PrestamoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTituloLibro)
        val tvAutor: TextView = view.findViewById(R.id.tvAutorLibro)
        val btnDevolver: View = view.findViewById(R.id.btnDevolverLibro)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_libro, parent, false)
        view.findViewById<View>(R.id.ivPortadaLibro).visibility = View.GONE
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val libro = libros[position]
        holder.tvTitulo.text = libro.titulo
        holder.tvAutor.text = libro.autor
        
        holder.btnDevolver.visibility = View.VISIBLE
        holder.btnDevolver.setOnClickListener { onDevolverClick(libro) }
    }

    override fun getItemCount() = libros.size

    fun updateData(newLibros: List<Libro>) {
        libros = newLibros
        notifyDataSetChanged()
    }
}