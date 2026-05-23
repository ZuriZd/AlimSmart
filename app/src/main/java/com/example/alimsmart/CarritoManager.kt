package com.example.alimsmart

object CarritoManager {
    private val listaCarrito = mutableListOf<Producto>()

    fun agregarProducto(producto: Producto) {
        listaCarrito.add(producto)
    }

    fun obtenerCarrito(): List<Producto> {
        return listaCarrito
    }

    fun limpiarCarrito() {
        listaCarrito.clear()
    }

    // LA NUEVA FUNCIÓN
    fun eliminarProducto(producto: Producto) {
        listaCarrito.remove(producto)
    }

    // NUEVA FUNCIÓN: Suma todos los precios automáticamente
    fun calcularTotal(): Double {
        var total = 0.0
        for (producto in listaCarrito) {
            total += (producto.precio * producto.cantidad)
        }
        return total
    }
}