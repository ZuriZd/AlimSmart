package com.example.alimsmart

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Este emplea técnicas de maquetado e inyección
 * programática para construir y renderizar componentes visuales de manera dinámica en tiempo de ejecución.
 */
class PedidosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos)

        supportActionBar?.hide()

        val tvVolverPedidos = findViewById<TextView>(R.id.tvVolverPedidos)
        val tvMensajePedidos = findViewById<TextView>(R.id.tvMensajePedidos)
        val layoutContenedor = findViewById<LinearLayout>(R.id.layoutContenedorPedidos)

        tvVolverPedidos.setOnClickListener { finish() }

        // Este evalua si existen transacciones registradas
        if (PedidoManager.listaPedidos.isEmpty()) {
            tvMensajePedidos.visibility = View.VISIBLE // Despliega la etiqueta informativa de lista vacía
        } else {
            tvMensajePedidos.visibility = View.GONE // Oculta la etiqueta de advertencia

            // Itera el historial del PedidoManager para inflar las tarjetas
            for (pedido in PedidoManager.listaPedidos) {

                // Instanciación del Contenedor Base (Estructura de Tarjeta / CardView)
                val tarjetaPedido = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(60, 50, 60, 50)

                    // Fondo tipo tarjeta moderna
                    setBackgroundResource(android.R.drawable.dialog_holo_light_frame)

                    // Elevación (sombra)
                    elevation = 12f

                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(30, 30, 30, 40)
                    layoutParams = params
                }

                // Muestra el número identificador único de la orden
                val tvId = TextView(this).apply {
                    text = "🧾 Orden ${pedido.id}"
                    textSize = 20f // Definición directa del tamaño en escala tipográfica SP mediante floats
                    setTextColor(Color.BLACK)
                    setPadding(0, 0, 0, 20)
                    setTypeface(typeface, android.graphics.Typeface.BOLD) // Aplica estilo tipográfico de énfasis (Negrita)
                }

                // Lista condensada de productos comprados
                val tvProductos = TextView(this).apply {
                    text = pedido.descripcion
                    textSize = 15f
                    setTextColor(Color.DKGRAY)
                    setPadding(0, 0, 0, 25)
                }

                val tvPago = TextView(this).apply {

                    text = if (pedido.metodoPago.contains("Tarjeta")) {
                        "💳 Tarjeta ****${pedido.ultimosDigitos}"
                    } else {
                        "💵 ${pedido.metodoPago}"
                    }

                    textSize = 14f
                    setTextColor(Color.parseColor("#2E7D32"))
                    setPadding(0, 0, 0, 25)
                }

                // Componente de Interacción (Botón): Gestiona los estados de la logística de entrega
                val btnRecibido = Button(this).apply {
                    textSize = 14f

                    // Mapeo de Persistencia Visual: Adapta la estética del botón si la orden fue cobrada anteriormente
                    if (pedido.recibido) {
                        text = "✓ Entregado"
                        isEnabled = false // Inhabilita interacciones repetitivas o redundantes
                        setBackgroundColor(Color.LTGRAY) // Aplica estilo visual gris inactivo
                        setTextColor(Color.DKGRAY)
                    } else {
                        text = "Marcar como recibido"
                        setBackgroundColor(Color.parseColor("#2E7D32")) // Aplica el verde temático corporativo
                        setTextColor(Color.WHITE)
                    }

                    setPadding(10, 10, 10, 10)

                    // Evento Click para componentes generados dinámicamente por código
                    setOnClickListener {
                        pedido.recibido = true // Actualiza de inmediato el modelo lógico global
                        text = "✓ Orden Recibida"
                        isEnabled = false
                        setBackgroundColor(Color.LTGRAY)
                    }
                }

                // Proceso de Inyección Estructurada: Ensambla los subcomponentes dentro del contenedor
                // de la tarjeta y posteriormente monta la estructura entera dentro del layout raíz del XML
                tarjetaPedido.addView(tvId)
                tarjetaPedido.addView(tvProductos)
                tarjetaPedido.addView(btnRecibido)
                layoutContenedor.addView(tarjetaPedido)
            }
        }
    }
}