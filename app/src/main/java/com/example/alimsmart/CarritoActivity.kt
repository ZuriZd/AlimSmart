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

        val productosAgregados = CarritoManager.obtenerProductos()

        rvCarrito.layoutManager = LinearLayoutManager(this)

        // AQUÍ ESTÁ LA MAGIA: Le pasamos 'true' para decirle que sí es el carrito
        productoAdapter = ProductoAdapter(productosAgregados, true){
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
            if (productosAgregados.isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "¡Compra realizada con éxito!", Toast.LENGTH_LONG).show()
                CarritoManager.limpiarCarrito()
                finish()
            }
        }
    }
}