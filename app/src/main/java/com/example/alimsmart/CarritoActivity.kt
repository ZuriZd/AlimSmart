package com.example.alimsmart

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Aqui se administra la vista de los productos seleccionados,
 * el recálculo dinámico de totales y la confirmación final para despachar órdenes al sistema.
 */
class CarritoActivity : AppCompatActivity() {

    private lateinit var tvTotalCompra: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)
        supportActionBar?.hide()

        // Vinculación de nodos UI
        tvTotalCompra = findViewById(R.id.tvTotalPagar)
        val btnFinalizarCompra = findViewById<Button>(R.id.btnFinalizarCompra)
        val tvVolverCarrito = findViewById<TextView>(R.id.tvVolver)

        // Configuración del adaptador reutilizable
        val recyclerView = findViewById<RecyclerView>(R.id.rvCarrito)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Se implementa un Callback (() -> Unit) para que el adaptador avise a esta Activity
        // cada vez que un producto es eliminado y sea necesario recalcular el texto del total.
        val adaptadorCarrito = ProductoAdapter(CarritoManager.obtenerCarrito(), true) {
            actualizarTotalUI()
        }
        recyclerView.adapter = adaptadorCarrito

        // Renderizado inicial del precio
        actualizarTotalUI()

        // Lógica de finalización de compra y creación de entidad Pedido
        btnFinalizarCompra.setOnClickListener {
            // Validación de estado: Previene procesamiento de órdenes vacías
            if (CarritoManager.obtenerCarrito().isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Generación de un hash/ID simulado para la orden comercial (En estos momentos no son IDS unicas solo aleatorias :( )
            val idRandom = "#${(10000..99999).random()}"

            // Aplana la lista de objetos a un string legible
            val resumenProductos = CarritoManager.obtenerCarrito().joinToString(", ") {
                "${it.nombre} (x${it.cantidad})"
            }

            // Persistencia temporal en el historial de transacciones (PedidoManager)
            val nuevoPedido = Pedido(idRandom, resumenProductos)
            PedidoManager.listaPedidos.add(nuevoPedido)

            // Limpieza de estado y retroalimentación al usuario
            CarritoManager.limpiarCarrito()
            Toast.makeText(this, "¡Pedido $idRandom realizado con éxito!", Toast.LENGTH_LONG).show()

            finish() // Cierra el flujo de compra y retorna al origen
        }

        tvVolverCarrito.setOnClickListener { finish() }
    }

    /**
     * Este consulta el modelo centralizado (CarritoManager) y actualiza el hilo de UI.
     */
    private fun actualizarTotalUI() {
        val total = CarritoManager.calcularTotal()
        tvTotalCompra.text = "Total: $$total"
    }
}