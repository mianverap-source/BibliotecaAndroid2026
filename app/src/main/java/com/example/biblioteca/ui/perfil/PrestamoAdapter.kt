package com.example.biblioteca.ui.perfil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.data.Libro

class PrestamoAdapter(private var libros: List<Libro>) : RecyclerView.Adapter<PrestamoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTituloLibro)
        val tvAutor: TextView = view.findViewById(R.id.tvAutorLibro)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_libro, parent, false)
        // Ocultar la imagen de portada ya que en Room no la guardamos por ahora
        view.findViewById<View>(R.id.ivPortadaLibro).visibility = View.GONE
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val libro = libros[position]
        holder.tvTitulo.text = libro.titulo
        holder.tvAutor.text = libro.autor
    }

    override fun getItemCount() = libros.size

    fun updateData(newLibros: List<Libro>) {
        libros = newLibros
        notifyDataSetChanged()
    }
}