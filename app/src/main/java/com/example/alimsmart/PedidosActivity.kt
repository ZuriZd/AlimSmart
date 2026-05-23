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
                    setPadding(32, 32, 32, 32)
                    setBackgroundColor(Color.WHITE)

                    // Configuración de dimensiones y comportamiento de layouts en código puro
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(0, 0, 0, 32) // Agrega separación física vertical entre órdenes
                    layoutParams = params
                }

                // Muestra el número identificador único de la orden
                val tvId = TextView(this).apply {
                    text = "Orden: ${pedido.id}"
                    textSize = 20f // Definición directa del tamaño en escala tipográfica SP mediante floats
                    typeface = android.graphics.Typeface.DEFAULT_BOLD // Aplica estilo tipográfico de énfasis (Negrita)
                    setTextColor(Color.BLACK)
                }

                // Lista condensada de productos comprados
                val tvProductos = TextView(this).apply {
                    text = pedido.descripcion
                    textSize = 16f
                    setTextColor(Color.GRAY)
                    setPadding(0, 8, 0, 16)
                }

                // Componente de Interacción (Botón): Gestiona los estados de la logística de entrega
                val btnRecibido = Button(this).apply {
                    textSize = 14f

                    // Mapeo de Persistencia Visual: Adapta la estética del botón si la orden fue cobrada anteriormente
                    if (pedido.recibido) {
                        text = "✓ Orden Recibida"
                        isEnabled = false // Inhabilita interacciones repetitivas o redundantes
                        setBackgroundColor(Color.LTGRAY) // Aplica estilo visual gris inactivo
                    } else {
                        text = "Marcar como Recibido"
                        setBackgroundColor(Color.parseColor("#2E7D32")) // Aplica el verde temático corporativo
                        setTextColor(Color.WHITE)
                    }

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
} //Hola soy Asa y me gusta el spaghetti mm que rico me voy a hacer uno al ratito. Compro
// Maruchan de bolsa y utilizo la pasta ya que me gusta la textura de este. Despues lo baño
//de una crema/salsa de chipotle que hago con leche condensada, chipotle enlatado, crema y caldo de pollo