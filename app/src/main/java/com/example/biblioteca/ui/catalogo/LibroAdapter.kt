package com.example.biblioteca.ui.catalogo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.biblioteca.R
import com.example.biblioteca.data.remote.BookDoc

class LibroAdapter(
    private var libros: List<BookDoc>,
    private val onLibroClick: (BookDoc) -> Unit
) : RecyclerView.Adapter<LibroAdapter.LibroViewHolder>() {

    class LibroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTituloLibro)
        val tvAutor: TextView = view.findViewById(R.id.tvAutorLibro)
        val ivPortada: ImageView = view.findViewById(R.id.ivPortadaLibro)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_libro, parent, false)
        return LibroViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibroViewHolder, position: Int) {
        val libro = libros[position]
        holder.tvTitulo.text = libro.title
        holder.tvAutor.text = libro.authorName?.firstOrNull() ?: "Autor desconocido"
        
        holder.ivPortada.load(libro.getCoverUrl()) {
            crossfade(true)
            placeholder(android.R.drawable.ic_menu_gallery)
            error(android.R.drawable.stat_notify_error)
        }

        holder.itemView.setOnClickListener { onLibroClick(libro) }
    }

    override fun getItemCount(): Int = libros.size

    fun updateData(newLibros: List<BookDoc>) {
        this.libros = newLibros
        notifyDataSetChanged()
    }
}