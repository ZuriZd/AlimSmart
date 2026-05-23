package com.example.alimsmart

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Aqui esta el famosisimo Main donde se hace funcionar
 * el motor de filtrado por categorías y la navegación base de la aplicación.
 */
class MainActivity : AppCompatActivity() {

    // Variables de estado de inicialización tardía
    private lateinit var listaCompletaProductos: List<Producto>
    private lateinit var productoAdapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide() // Limpieza de UI

        // 1. Población del Modelo de Datos Maestro (Mock Data para el catálogo)
        listaCompletaProductos = listOf(
            Producto(id = 1, nombre = "Manzana Roja", precio = 20.0, imagen = R.drawable.manzana_roja, categoria = "Frutas"),
            Producto(id = 2, nombre = "Leche Alpura 1l", precio = 30.0, imagen = R.drawable.leche_alpura_1l, categoria = "Lácteos"),
            Producto(id = 3, nombre = "Manzana Verde", precio = 25.0, imagen = R.drawable.manzana_verde, categoria = "Frutas"),
            Producto(id = 4, nombre = "Refresco Coca Cola 600ml", precio = 24.0, imagen = R.drawable.refresco_coca_600, categoria = "Bebidas")
        )

        // 2. Configuración del Patrón Reciclador (RecyclerView)
        val recyclerView = findViewById<RecyclerView>(R.id.rvProductos)
        // Se define un LayoutManager lineal para apilar las tarjetas verticalmente
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Se pasa la lista al Adapter para que inicie el renderizado
        productoAdapter = ProductoAdapter(listaCompletaProductos)
        recyclerView.adapter = productoAdapter

        // 3. Sistema de Filtros Reactivos (Event Listeners)
        val btnCatTodos = findViewById<Button>(R.id.btnCatTodos)
        val btnCatFrutas = findViewById<Button>(R.id.btnCatFrutas)
        val btnCatLacteos = findViewById<Button>(R.id.btnCatLacteos)
        val btnCatBebidas = findViewById<Button>(R.id.btnCatBebidas)

        // Restauración de la vista completa
        btnCatTodos.setOnClickListener {
            productoAdapter.actualizarLista(listaCompletaProductos)
        }

        // Aplicación de función de orden superior (filter) para mutar la vista

        btnCatFrutas.setOnClickListener {
            val filtrados = listaCompletaProductos.filter { it.categoria == "Frutas" }
            productoAdapter.actualizarLista(filtrados)
        }

        btnCatLacteos.setOnClickListener {
            val filtrados = listaCompletaProductos.filter { it.categoria == "Lácteos" }
            productoAdapter.actualizarLista(filtrados)
        }

        btnCatBebidas.setOnClickListener {
            val filtrados = listaCompletaProductos.filter { it.categoria == "Bebidas" }
            productoAdapter.actualizarLista(filtrados)
        }

        // 4. Controlador del Grafo de Navegación (BottomNavigationView)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.nav_inicio // Marca el ícono actual como activo

        // Intercepta los toques en el menú inferior y despacha Intents explícitos
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> true // Previene recargar la pantalla si ya estamos en ella
                R.id.nav_carrito -> {
                    startActivity(Intent(this, CarritoActivity::class.java))
                    true
                }
                R.id.nav_pedidos -> {
                    startActivity(Intent(this, PedidosActivity::class.java))
                    true
                }
                R.id.nav_perfil -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}