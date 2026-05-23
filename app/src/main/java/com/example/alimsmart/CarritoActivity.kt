package com.example.alimsmart

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CarritoActivity : AppCompatActivity() {

    private lateinit var rvCarrito: RecyclerView
    private lateinit var tvTotalPagar: TextView
    private lateinit var btnFinalizarCompra: Button
    private lateinit var tvVolver: TextView // Nueva variable
    private lateinit var productoAdapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        supportActionBar?.hide()

        rvCarrito = findViewById(R.id.rvCarrito)
        tvTotalPagar = findViewById(R.id.tvTotalPagar)
        btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra)
        tvVolver = findViewById(R.id.tvVolver) // Vinculamos el botón de volver

        val lista = CarritoManager.obtenerCarrito()

        rvCarrito.layoutManager = LinearLayoutManager(this)

        // AQUÍ ESTÁ LA MAGIA: Le pasamos 'true' para decirle que sí es el carrito
        productoAdapter = ProductoAdapter(lista, true){
            // Este bloque se ejecuta SOLAMENTE cuando el usuario presiona "Quitar"
            tvTotalPagar.text = "Total: $${CarritoManager.calcularTotal()}"
        }
        rvCarrito.adapter = productoAdapter

        tvTotalPagar.text = "Total: $${CarritoManager.calcularTotal()}"

        // Lógica para salir de la pantalla en cualquier momento
        tvVolver.setOnClickListener {
            finish()
        }

        btnFinalizarCompra.setOnClickListener {

            // 1. Validamos que el carrito no esté vacío
            if (CarritoManager.obtenerCarrito().isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Generamos un ID aleatorio de 5 dígitos (Ej: #48291)
            val idRandom = "#${(10000..99999).random()}"

            // 3. Juntamos los nombres de los productos para la descripción del pedido
            val resumenProductos = CarritoManager.obtenerCarrito().joinToString(", ") {
                "${it.nombre} (x${it.cantidad})"
            }

            // 4. Creamos el pedido y lo guardamos en nuestro nuevo Manager global
            val nuevoPedido = Pedido(idRandom, resumenProductos)
            PedidoManager.listaPedidos.add(nuevoPedido)

            // 5. Limpiamos el carrito temporal para que quede vacío de nuevo
            CarritoManager.limpiarCarrito()

            Toast.makeText(this, "¡Pedido $idRandom realizado!", Toast.LENGTH_LONG).show()

            // 6. Cerramos la pantalla del carrito para regresar automáticamente
            finish()
            }
        }
    }
