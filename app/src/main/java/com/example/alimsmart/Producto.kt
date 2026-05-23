package com.example.alimsmart

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val imagenResId: Int, // Guarda la referencia numérica de la imagen (ej. R.drawable.manzana_roja)
    val categoria: String
)