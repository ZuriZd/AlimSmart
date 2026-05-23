package com.example.alimsmart

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Aqui esta lo que controla el carrito
 * Esta pantalla maneja la lista de compras actual, calcula el precio total
 * y ejecuta el flujo de validación de pago antes de generar un pedido.
 */
class CarritoActivity : AppCompatActivity() {

    private lateinit var tvTotalCompra: TextView

    // Declaramos el conector de la Base de Datos a nivel de clase para usarlo en las validaciones de las tarjetas
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)
        supportActionBar?.hide()

        // Inicializamos el ayudante de la Base de Datos SQLite
        dbHelper = DatabaseHelper(this)

        // Vinculación de los componentes del diseño XML
        tvTotalCompra = findViewById(R.id.tvTotalPagar)
        val btnFinalizarCompra = findViewById<Button>(R.id.btnFinalizarCompra)
        val tvVolverCarrito = findViewById<TextView>(R.id.tvVolver)

        // Configuración del RecyclerView para mostrar los productos en el carrito
        val recyclerView = findViewById<RecyclerView>(R.id.rvCarrito)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // El adaptador recibe la lista actual y una función lambda para recalcular el total si borramos un artículo
        val adaptadorCarrito = ProductoAdapter(CarritoManager.obtenerCarrito(), true) {
            actualizarTotalUI()
        }
        recyclerView.adapter = adaptadorCarrito

        // Renderizado inicial del precio total acumulado
        actualizarTotalUI()


        btnFinalizarCompra.setOnClickListener {
            if (CarritoManager.obtenerCarrito().isEmpty()) {
                Toast.makeText(this, "Carrito vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (SessionManager.esInvitado) {
                // ==========================================
                // RUTA A: INVITADO (Directo a Efectivo)
                // ==========================================
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Confirmar Pedido")
                    .setMessage("Como invitado, tu pago será en EFECTIVO al recibir. ¿Confirmar?")
                    .setPositiveButton("Sí, pedir") { _, _ ->
                        // Descuento 0.0 porque es invitado
                        procesarPedidoFinal(metodoElegido = "Efectivo", descuentoAplicado = 0.0)
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            } else {

                // RUTA B: USUARIO REGISTRADO
                // 1. Mostrar menú de cupones
                val opcionesCupon = CuponManager.listaCupones.map { it.nombre }.toMutableList()
                opcionesCupon.add(0, "No usar cupón") // Agregamos opción vacía al inicio

                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("¡Eres usuario VIP! Elige un cupón:")
                    .setItems(opcionesCupon.toTypedArray()) { _, posicion ->

                        // Obtenemos el descuento dependiendo de su elección
                        val descuento = if (posicion == 0) 0.0 else CuponManager.listaCupones[posicion - 1].descuento

                        // 2. Mostrar menú de métodos de pago
                        val metodos = arrayOf("Efectivo (Al recibir)", "Tarjeta Guardada")
                        androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Método de Pago")
                            .setItems(metodos) { _, metodoPos ->
                                if (metodoPos == 0) {
                                    // Eligió Efectivo
                                    procesarPedidoFinal("Efectivo", descuento)
                                } else {
                                    // Eligió Tarjeta -> Validamos en SQLite
                                    val tarjetas = dbHelper.obtenerTarjetasUsuario(SessionManager.nombreUsuario!!)
                                    if (tarjetas.isEmpty()) {
                                        Toast.makeText(this, "No tienes tarjetas. Ve a Perfil.", Toast.LENGTH_LONG).show()
                                        startActivity(Intent(this, PerfilActivity::class.java))
                                    } else {
                                        // Procesamos con la primera tarjeta registrada
                                        procesarPedidoFinal("Tarjeta (${tarjetas[0]})", descuento)
                                    }
                                }
                            }.show()
                    }.show()
            }
        }

        // Botón de escape para volver a la pantalla principal
        tvVolverCarrito.setOnClickListener { finish() }
    }

    /**
     * MÉTODOS AUXILIARES DE LA CLASE
     */
    // Actualiza el componente de texto del precio en la interfaz gráfica
    private fun actualizarTotalUI() {
        val total = CarritoManager.calcularTotal()
        tvTotalCompra.text = "Total: $$total"
    }

    /**
     * Logica central
     * Esta función automatiza los pasos repetitivos de la compra una vez que
     * el metodo de pago ha sido aprobado por los filtros anteriores.
     */
    private fun procesarPedidoFinal(metodoElegido: String, descuentoAplicado: Double) {
        val idRandom = "#${(10000..99999).random()}"

        // Matemáticas del descuento
        val totalOriginal = CarritoManager.calcularTotal()
        val totalConDescuento = totalOriginal * (1.0 - descuentoAplicado)

        // Construimos el resumen final
        val resumen = CarritoManager.obtenerCarrito().joinToString(", ") {
            "${it.nombre} (x${it.cantidad})"
        } + "\n💳 Pago: $metodoElegido \n💰 Total Pagado: $$totalConDescuento"

        val nuevoPedido = Pedido(idRandom, resumen)
        PedidoManager.listaPedidos.add(nuevoPedido)

        CarritoManager.limpiarCarrito()
        Toast.makeText(this, "¡Pedido $idRandom realizado!", Toast.LENGTH_LONG).show()
        finish()
    }
}