package com.example.alimsmart

object CarritoManager {

    // Una lista mutable (que puede cambiar) privada para guardar los productos
    private val productosEnCarrito = mutableListOf<Producto>()

    // Función para añadir un producto
    fun agregarProducto(producto: Producto) {
        productosEnCarrito.add(producto)
    }

    // Función para quitar un producto
    fun eliminarProducto(producto: Producto) {
        productosEnCarrito.remove(producto)
    }

    // Función para leer qué hay en el carrito
    fun obtenerProductos(): List<Producto> {
        return productosEnCarrito
    }

    // Función súper útil de Kotlin que suma automáticamente todos los precios
    fun calcularTotal(): Double {
        return productosEnCarrito.sumOf { it.precio }
    }

    // Para vaciar el carrito después de pagar
    fun limpiarCarrito() {
        productosEnCarrito.clear()

    }
}