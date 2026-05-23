package com.example.alimsmart

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ProductoAdapter(
    private var listaProductos: List<Producto>,
    private val esCarrito: Boolean = false,
    private val onCambioCarrito: (() -> Unit)? = null // Canal de comunicación con la Activity
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImagenProducto: ImageView = itemView.findViewById(R.id.ivImagenProducto)
        val tvNombreProducto: TextView = itemView.findViewById(R.id.tvNombreProducto)
        val tvPrecioProducto: TextView = itemView.findViewById(R.id.tvPrecioProducto)
        val btnAgregarCarrito: Button = itemView.findViewById(R.id.btnAgregarCarrito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = listaProductos[position]

        holder.tvNombreProducto.text = producto.nombre
        holder.tvPrecioProducto.text = "$${producto.precio}"
        holder.ivImagenProducto.setImageResource(producto.imagen)

        // MAGIA VISUAL: Si es el carrito, reciclamos el botón para que sea de eliminar
        if (esCarrito) {
            holder.btnAgregarCarrito.visibility = View.VISIBLE
            holder.btnAgregarCarrito.text = "Quitar"
            holder.btnAgregarCarrito.setBackgroundColor(Color.parseColor("#E53935")) // Color rojo

            holder.btnAgregarCarrito.setOnClickListener {
                // 1. Lo borramos de la memoria
                CarritoManager.eliminarProducto(producto)
                // 2. Actualizamos la lista visual
                actualizarLista(CarritoManager.obtenerCarrito())
                // 3. Avisamos a la Activity para que recalcule el total
                onCambioCarrito?.invoke()

                Toast.makeText(holder.itemView.context, "Producto eliminado", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Si estamos en la pantalla principal, el botón es verde y agrega productos
            holder.btnAgregarCarrito.visibility = View.VISIBLE
            holder.btnAgregarCarrito.text = "Agregar al carrito"
            holder.btnAgregarCarrito.setBackgroundColor(Color.parseColor("#43A047")) // Color verde

            holder.btnAgregarCarrito.setOnClickListener {
                CarritoManager.agregarProducto(producto)
                Toast.makeText(holder.itemView.context, "${producto.nombre} se agregó", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return listaProductos.size
    }

    fun actualizarLista(nuevaLista: List<Producto>) {
        listaProductos = nuevaLista
        notifyDataSetChanged()
    }
}