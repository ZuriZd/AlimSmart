package com.example.alimsmart

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val imagen: Int,
    val categoria: String, // Agregamos la categoría para que los filtros funcionen
    var cantidad: Int = 1  // Dejamos la cantidad para que el carrito funcione
)