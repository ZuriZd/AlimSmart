package com.example.alimsmart

/**
 * El gestor de carrito utiliza Singleton para administra la cesta de productos aplicando
 * el principio de encapsulamiento. La lista de datos interna está blindada mediante el modificador
 * de acceso 'private', obligando a las interfaces externas a utilizar únicamente las funciones autorizadas.
 */
object CarritoManager {
    // Contenedor interno privado protegido contra modificaciones directas desde adaptadores o actividades
    private val listaCarrito = mutableListOf<Producto>()

    /**
     * Inserta un artículo seleccionado por el usuario dentro del carrito de compras.
     */
    fun agregarProducto(producto: Producto) {
        listaCarrito.add(producto)
    }

    /**
     * Proporciona una lectura controlada en formato de lista inmutable para el mapeo del RecyclerView.
     */
    fun obtenerCarrito(): List<Producto> {
        return listaCarrito
    }

    /**
     * Remueve un artículo específico de la cesta cuando el usuario decrementa su selección.
     */
    fun eliminarProducto(producto: Producto) {
        listaCarrito.remove(producto)
    }

    /**
     * Vacía por completo la cesta tras la confirmación exitosa de un pedido.
     */
    fun limpiarCarrito() {
        listaCarrito.clear()
    }

    /**
     * Calcuila el precio total de todos los articulos!
     */
    fun calcularTotal(): Double {
        var total = 0.0
        for (producto in listaCarrito) {
            total += (producto.precio * producto.cantidad)
        }
        return total
    }
}