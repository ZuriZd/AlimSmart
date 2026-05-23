package com.example.alimsmart

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PedidosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos)

        supportActionBar?.hide()

        val tvVolverPedidos = findViewById<TextView>(R.id.tvVolverPedidos)
        val tvMensajePedidos = findViewById<TextView>(R.id.tvMensajePedidos)
        val layoutContenedor = findViewById<LinearLayout>(R.id.layoutContenedorPedidos)

        tvVolverPedidos.setOnClickListener { finish() }

        // 1. Validamos si hay o no pedidos en el PedidoManager
        if (PedidoManager.listaPedidos.isEmpty()) {
            tvMensajePedidos.visibility = View.VISIBLE
        } else {
            tvMensajePedidos.visibility = View.GONE

            // 2. Iteramos cada orden guardada en el gestor para dibujarla en pantalla
            for (pedido in PedidoManager.listaPedidos) {

                // Creamos un contenedor individual (tarjeta) para el pedido
                val tarjetaPedido = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(32, 32, 32, 32)
                    setBackgroundColor(Color.WHITE)
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(0, 0, 0, 32) // Margen inferior entre tarjetas
                    layoutParams = params
                }

                // Texto del ID del pedido
                val tvId = TextView(this).apply {
                    text = "Orden: ${pedido.id}"
                    textSize = 20f
                    typeface = android.graphics.Typeface.DEFAULT_BOLD
                    setTextColor(Color.BLACK)
                }

                // Texto con los productos
                val tvProductos = TextView(this).apply {
                    text = pedido.descripcion
                    textSize = 16f
                            setTextColor(Color.GRAY)
                    setPadding(0, 8, 0, 16)
                }

                // Botón de Recibido
                val btnRecibido = Button(this).apply {
                    textSize = 14f

                            // Si el pedido ya había sido recibido previamente, ajustamos su diseño
                            if (pedido.recibido) {
                                text = "✓ Orden Recibida"
                                isEnabled = false
                                setBackgroundColor(Color.LTGRAY)
                            } else {
                                text = "Marcar como Recibido"
                                setBackgroundColor(Color.parseColor("#2E7D32")) // Verde AlimSmart
                                setTextColor(Color.WHITE)
                            }

                    // Acción al presionar el botón de recibido
                    setOnClickListener {
                        pedido.recibido = true // Cambiamos el estado en el Manager
                        text = "✓ Orden Recibida"
                        isEnabled = false
                        setBackgroundColor(Color.LTGRAY)
                    }
                }

                // Agregamos los componentes a la tarjeta, y la tarjeta al contenedor principal
                tarjetaPedido.addView(tvId)
                tarjetaPedido.addView(tvProductos)
                tarjetaPedido.addView(btnRecibido)
                layoutContenedor.addView(tarjetaPedido)
            }
        }
    }
}