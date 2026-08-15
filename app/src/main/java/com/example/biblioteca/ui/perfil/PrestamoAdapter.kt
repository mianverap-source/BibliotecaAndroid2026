package com.example.biblioteca.ui.perfil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.R
import com.example.biblioteca.data.Libro
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrestamoAdapter(
    private var libros: List<Libro>,
    private val onDevolverClick: (Libro) -> Unit,
    private val onEditarClick: (Libro) -> Unit,
    private val isHistorial: Boolean = false
) : RecyclerView.Adapter<PrestamoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTituloLibro)
        val tvAutor: TextView = view.findViewById(R.id.tvAutorLibro)
        val tvCategoria: TextView = view.findViewById(R.id.tvCategoriaLibro)
        val tvFecha: TextView = view.findViewById(R.id.tvFechaLibro)
        val tvAlerta: TextView = view.findViewById(R.id.tvAlertaLibro)
        val btnDevolver: View = view.findViewById(R.id.btnDevolverLibro)
        val btnEditar: View = view.findViewById(R.id.btnEditarPrestamo)
        val btnEliminar: View = view.findViewById(R.id.btnEliminarPrestamo)
        val layoutActions: View = view.findViewById(R.id.layoutActions)
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
        holder.tvCategoria.text = libro.categoriaNombre ?: "General"
        holder.tvCategoria.visibility = View.VISIBLE
        
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.tvFecha.text = "Pedido el: ${sdf.format(Date(libro.fechaPrestamo))}"
        holder.tvFecha.visibility = View.VISIBLE

        if (!isHistorial) {
            holder.tvAlerta.visibility = View.VISIBLE
            holder.layoutActions.visibility = View.VISIBLE
            holder.btnDevolver.visibility = View.VISIBLE
            holder.btnEliminar.visibility = View.GONE // Eliminamos el botón de eliminar físico, ahora es Devolver
            
            holder.btnDevolver.setOnClickListener { onDevolverClick(libro) }
            holder.btnEditar.setOnClickListener { onEditarClick(libro) }
        } else {
            holder.tvAlerta.visibility = View.GONE
            holder.layoutActions.visibility = View.GONE
            holder.btnDevolver.visibility = View.GONE
            holder.tvFecha.text = "Devuelto el: ${sdf.format(Date(libro.fechaPrestamo))}" // Reusamos campo para historial
        }
    }

    override fun getItemCount() = libros.size

    fun updateData(newLibros: List<Libro>) {
        libros = newLibros
        notifyDataSetChanged()
    }
}