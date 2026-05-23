package com.example.alimsmart

// Modelo para saber qué estructura tiene cada orden
data class Pedido(
    val id: String,
    val descripcion: String,
    var recibido: Boolean = false
)

object PedidoManager {
    // Lista global que almacenará los pedidos realizados
    val listaPedidos = mutableListOf<Pedido>()
}