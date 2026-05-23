package com.example.alimsmart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Esto es un Adaptater, funciona como un puente entre la fuente de datos (la lista de productos)
 * y la interfaz visual (RecyclerView). Su función principal es reciclar las vistas de la memoria
 * conforme el usuario hace scroll, optimizando el rendimiento y evitando el consumo excesivo de RAM.
 *
 * Simplificando, hace un puente entre la lista de productor y la interefaz para recilar vista y que
 * la app corra mejor
 */
class ProductoAdapter(
    private var listaProductos: List<Producto>,
    private val esCarrito: Boolean = false, // Bandera lógica para reutilizar el adaptador en dos pantallas distintas
    private val onCambioCarrito: (() -> Unit)? = null // Función de callback para notificar cambios de precio
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    /**
     * Esta parte almacena las referencias físicas de los componentes de la interfaz (XML)
     * de un solo elemento de la lista. Evita realizar costosas llamadas a 'findViewById' repetidamente.
     */
    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImagenProducto: ImageView = itemView.findViewById(R.id.ivImagenProducto)
        val tvNombreProducto: TextView = itemView.findViewById(R.id.tvNombreProducto)
        val tvPrecioProducto: TextView = itemView.findViewById(R.id.tvPrecioProducto)
        val btnAgregarCarrito: Button = itemView.findViewById(R.id.btnAgregarCarrito)
    }

    /**
     * Aqui se infla (convierte el XML en objetos de código) el diseño base
     * de una tarjeta individual solo cuando no hay tarjetas disponibles para reciclar.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(vista)
    }

    /**
     * Inyecta dinámicamente los datos del producto en una
     * tarjeta reciclada existente que está a punto de entrar a la pantalla por el scroll.
     */
    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = listaProductos[position]

        // Mapeo de datos al componente visual
        holder.tvNombreProducto.text = producto.nombre
        holder.tvPrecioProducto.text = "$${producto.precio}"
        holder.ivImagenProducto.setImageResource(producto.imagen)

        // Comportamiento dinámico según el contexto (Pantalla Principal vs. Pantalla Carrito)
        if (esCarrito) {
            // Lógica para modo Carrito: El botón se transforma en "Quitar"
            holder.btnAgregarCarrito.text = "Quitar"
            holder.btnAgregarCarrito.setBackgroundColor(android.graphics.Color.parseColor("#E53935"))

            holder.btnAgregarCarrito.setOnClickListener {
                CarritoManager.eliminarProducto(producto)
                actualizarLista(CarritoManager.obtenerCarrito())
                onCambioCarrito?.invoke()
                android.widget.Toast.makeText(holder.itemView.context, "Producto eliminado", android.widget.Toast.LENGTH_SHORT).show()
            }
        } else {
            // Lógica para modo Catálogo: El botón agrega al Manager
            holder.btnAgregarCarrito.text = "Agregar"
            holder.btnAgregarCarrito.setBackgroundColor(android.graphics.Color.parseColor("#2E7D32")) // Verde

            holder.btnAgregarCarrito.setOnClickListener {
                CarritoManager.agregarProducto(producto)
                android.widget.Toast.makeText(holder.itemView.context, "${producto.nombre} agregado", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = listaProductos.size

    /**
     * Este refresca el conjunto de datos interno y notifica al RecyclerView
     * que debe volver a dibujar la pantalla. Esencial para los filtros de búsqueda por categorías.
     */
    fun actualizarLista(nuevaLista: List<Producto>) {
        listaProductos = nuevaLista
        notifyDataSetChanged()
    }
}