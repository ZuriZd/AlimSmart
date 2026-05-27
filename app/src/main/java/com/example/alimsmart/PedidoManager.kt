package com.example.alimsmart

/**
 * El data class esta diseñada exclusivamente como un contenedor inmutable
 * para transportar la información de una orden.
 */
data class Pedido(
    val id: String,               // Genera un numero al azar para los pedidos
    val descripcion: String,      // Resumen formateado en cadena de los productos y cantidades compradas
    var recibido: Boolean = false, // Estado lógico del paquete en el proceso de entrega física
    val metodoPago: String = "",
    val ultimosDigitos: String = ""
)

/**
 * Utilizando SIngleton este centraliza la lista en memoria dinámica que recolecta
 * todas las compras validadas y enviadas desde la pantalla del carrito de compras.
 */
object PedidoManager {
    // Lista de manipulación mutable que permite inserciones asíncronas en tiempo real
    val listaPedidos = mutableListOf<Pedido>()
}