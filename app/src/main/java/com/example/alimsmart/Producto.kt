package com.example.alimsmart

/**
 * Este representa la estructura de un artículo dentro de la aplicación.
 * Al usar 'data class', el compilador genera automáticamente los métodos boilerplate
 * (getters, setters, equals, hashCode y copy), optimizando el código (que cool, no?)
 */
data class Producto(
    val id: Int,              // Identificador único del producto en el catálogo
    val nombre: String,       // Cadena de texto con el nombre comercial
    val precio: Double,       // Valor monetario de punto flotante para cálculos precisos
    val imagen: Int,          // Referencia entera (Resource ID) al archivo drawable/mipmap
    val categoria: String,    // Criterio de agrupación para el motor de filtrado del catálogo
    var cantidad: Int = 1     // Estado mutable: Valor por defecto inicializado en 1 para el carrito
)