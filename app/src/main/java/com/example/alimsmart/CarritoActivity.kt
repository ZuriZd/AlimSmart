package com.example.alimsmart

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import android.content.Intent

class CarritoActivity : AppCompatActivity() {

    private lateinit var rvCarrito: RecyclerView
    private lateinit var tvTotalPagar: TextView
    private lateinit var btnFinalizarCompra: Button
    private lateinit var tvVolver: ImageView // Nueva variable
    private lateinit var productoAdapter: ProductoAdapter
    private lateinit var dbHelper: DatabaseHelper
    private var esperandoTarjeta = false
    private var idTemporal = ""
    private var descuentoTemporal = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        supportActionBar?.hide()

        rvCarrito = findViewById(R.id.rvCarrito)
        tvTotalPagar = findViewById(R.id.tvTotalPagar)
        btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra)
        tvVolver = findViewById(R.id.tvVolver) // Vinculamos el botón de volver
        dbHelper = DatabaseHelper(this)

        val lista = CarritoManager.obtenerCarrito()

        rvCarrito.layoutManager = LinearLayoutManager(this)

        // AQUÍ ESTÁ LA MAGIA: Le pasamos 'true' para decirle que sí es el carrito
        productoAdapter = ProductoAdapter(lista, true) {
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

            if (CarritoManager.obtenerCarrito().isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val idRandom = "#${(10000..99999).random()}"

            // =========================
            // CASO INVITADO
            // =========================
            if (SessionManager.obtenerUsuario() == "Invitado") {

                AlertDialog.Builder(this)
                    .setTitle("Confirmar pedido")
                    .setMessage("Como invitado pagarás en EFECTIVO. ¿Confirmar?")
                    .setPositiveButton("Sí") { _, _ ->

                        procesarPedido(idRandom, "Efectivo", 0.0)
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()

                return@setOnClickListener
            }

            // =========================
            // CASO USUARIO REGISTRADO
            // =========================

            val opcionesCupon = CuponManager.listaCupones.map { it.nombre }.toMutableList()
            opcionesCupon.add(0, "No usar cupón")

            AlertDialog.Builder(this)
                .setTitle("Elige un cupón")
                .setItems(opcionesCupon.toTypedArray()) { _, pos ->

                    val descuento = if (pos == 0) 0.0
                    else CuponManager.listaCupones[pos - 1].descuento

                    val metodos = arrayOf("Efectivo", "Tarjeta Guardada")

                    AlertDialog.Builder(this)
                        .setTitle("Método de pago")
                        .setItems(metodos) { _, metodo ->

                            if (metodo == 0) {

                                procesarPedido(idRandom, "Efectivo", descuento)

                            } else {

                                val tarjetas =
                                    dbHelper.obtenerTarjetasUsuario(SessionManager.nombreUsuario!!)

                                if (tarjetas.isEmpty()) {

                                    esperandoTarjeta = true
                                    idTemporal = idRandom
                                    descuentoTemporal = descuento

                                    Toast.makeText(this, "Registra una tarjeta para continuar", Toast.LENGTH_LONG).show()

                                    startActivity(Intent(this, PerfilActivity::class.java))
                                    return@setItems
                                }

                                procesarPedido(idRandom, "Tarjeta (${tarjetas[0]})", descuento)
                            }
                        }
                        .show()
                }
                .show()
        }

    }

    private fun procesarPedido(id: String, metodo: String, descuento: Double) {

        val totalOriginal = CarritoManager.calcularTotal()
        val totalFinal = totalOriginal * (1.0 - descuento)

        val resumen = CarritoManager.obtenerCarrito()
            .groupBy { it.nombre }
            .map { (nombre, items) ->
                val cantidadTotal = items.sumOf { it.cantidad }
                "• $nombre (x$cantidadTotal)"
            }
            .joinToString("\n") + "\n\n💳 Pago: $metodo\n💰 Total: $totalFinal"

        val pedido = Pedido(id, resumen)
        PedidoManager.listaPedidos.add(pedido)

        CarritoManager.limpiarCarrito()

        Toast.makeText(this, "¡Pedido $id realizado!", Toast.LENGTH_LONG).show()

        finish()
    }

    override fun onResume() {
        super.onResume()

        if (esperandoTarjeta) {

            val tarjetas = dbHelper.obtenerTarjetasUsuario(SessionManager.nombreUsuario!!)

            if (tarjetas.isNotEmpty()) {

                procesarPedido(
                    idTemporal,
                    "Tarjeta (${tarjetas[0].takeLast(4)})",
                    descuentoTemporal
                )

                esperandoTarjeta = false
            }
        }
    }
}
