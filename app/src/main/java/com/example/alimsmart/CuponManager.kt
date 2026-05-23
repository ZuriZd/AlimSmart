package com.example.alimsmart

// 1. Estructura de datos (Debe ir afuera del object)
data class Cupon(val nombre: String, val descuento: Double)

// 2. El Gestor Global (Debe decir 'object', NO 'class')
object CuponManager {
    val listaCupones = listOf(
        Cupon("BIENVENIDA20 (20% OFF)", 0.20),
        Cupon("ALIMFREE (10% OFF)", 0.10),
        Cupon("SMART50 (50% OFF)", 0.50)
    )
}