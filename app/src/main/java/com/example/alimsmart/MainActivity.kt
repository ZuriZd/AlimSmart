package com.example.alimsmart

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var etBuscador: EditText
    private lateinit var rvProductos: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter

    // Aquí guardamos la lista completa de productos que se cargarán en la app
    private lateinit var listaCompletaProductos: List<Producto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Inicializar los productos (Simulando lo que vendría de una base de datos)
        // Usamos los mismos nombres e imágenes que ya tenía el proyecto original
        listaCompletaProductos = listOf(
            Producto(id = 1, nombre = "Manzana Roja", precio = 20.0, imagen = R.drawable.manzana_roja, categoria = "Frutas"),
            Producto(id = 2, nombre = "Leche Alpura 1l", precio = 30.0, imagen = R.drawable.leche_alpura_1l, categoria = "Lácteos"),
            Producto(id = 3, nombre = "Manzana Verde", precio = 25.0, imagen = R.drawable.manzana_verde, categoria = "Frutas"),
            Producto(id = 4, nombre = "Refresco Coca Cola 600ml", precio = 24.0, imagen = R.drawable.refresco_coca_600, categoria = "Bebidas")
        )

        // 2. Vincular vistas de la pantalla principal
        etBuscador = findViewById(R.id.etBuscador)
        rvProductos = findViewById(R.id.rvProductos)

        // 3. Configurar el RecyclerView (El contenedor dinámico)
        // LinearLayoutManager le dice que se comporte como una lista vertical (una columna)
        rvProductos.layoutManager = LinearLayoutManager(this)

        // Creamos el adaptador pasándole la lista completa y se lo asignamos al RecyclerView
        productoAdapter = ProductoAdapter(listaCompletaProductos)
        rvProductos.adapter = productoAdapter

        // 4. Vincular los botones de categorías
        val btnCatTodos = findViewById<Button>(R.id.btnCatTodos)
        val btnCatFrutas = findViewById<Button>(R.id.btnCatFrutas)
        val btnCatLacteos = findViewById<Button>(R.id.btnCatLacteos)
        val btnCatBebidas = findViewById<Button>(R.id.btnCatBebidas)


        // 5. LÓGICA DE FILTRADO POR CATEGORÍAS

        btnCatTodos.setOnClickListener {
            // Le devolvemos al adaptador la lista original sin filtros
            productoAdapter.actualizarLista(listaCompletaProductos)
        }

        // Filtramos la lista original en una sola línea y se la mandamos al adaptador
        btnCatFrutas.setOnClickListener {
            val filtrados = listaCompletaProductos.filter { it.categoria == "Frutas" } // Ajusta según tu lógica
            productoAdapter.actualizarLista(filtrados)
        }
        btnCatLacteos.setOnClickListener {
            val listaFiltrada = listaCompletaProductos.filter { it.categoria == "Lácteos" }
            productoAdapter.actualizarLista(listaFiltrada)
        }

        btnCatBebidas.setOnClickListener {
            val listaFiltrada = listaCompletaProductos.filter { it.categoria == "Bebidas" }
            productoAdapter.actualizarLista(listaFiltrada)
        }

        // 6. LÓGICA DEL BUSCADOR DINÁMICO
        etBuscador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val textoBusqueda = s.toString().lowercase().trim()

                // Filtra los productos cuyo nombre contenga las letras escritas
                val listaFiltrada = listaCompletaProductos.filter {
                    it.nombre.lowercase().contains(textoBusqueda)
                }

                // Le pasamos los resultados de la búsqueda al adaptador para que se redibuje
                productoAdapter.actualizarLista(listaFiltrada)
            }
        })

        // 7. LÓGICA DEL MENÚ INFERIOR
        val bottomNavigation = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    productoAdapter.actualizarLista(listaCompletaProductos)
                    true
                }
                // AH0RA ABRE EL CARRITO
                R.id.nav_carrito -> {
                    val intent = Intent(this, CarritoActivity::class.java)
                    startActivity(intent)
                    true
                }
                // AHORA ABRE EL HISTORIAL DE PEDIDOS
                R.id.nav_pedidos -> {
                    val intent = Intent(this, PedidosActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_perfil -> {
                    val intent = Intent(this, PerfilActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}