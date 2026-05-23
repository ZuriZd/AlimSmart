package com.example.alimsmart

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Gestor de Base de Datos, esta clase encargada de inicializar, crear y
 * versionar la base de datos local (SQLite) del dispositivo.
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Constantes de configuración estructural de la base de datos
        private const val DATABASE_NAME = "AlimSmartDB.db"
        private const val DATABASE_VERSION = 1 // Útil para gestionar migraciones futuras de tablas
    }

    /**
     * DDL se ejecuta automáticamente la primera vez que la app
     * solicita acceso a la base de datos. Construye el esquema relacional de las tablas.
     */
    override fun onCreate(db: SQLiteDatabase?) {
        // Creación de la tabla 'usuarios' con llave primaria autoincremental
        // Itente moverlo a dos lineas diferentes pero solo no me dejaba y se rompia, entonces se queda asi
        val createTable = "CREATE TABLE usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, usuario TEXT, correo TEXT, password TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Lógica de migración: Se eliminaría la tabla vieja y se llamaría a onCreate nuevamente
        db?.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    /**
     * Aqui se transforma los datos recibidos en un mapa de valores (ContentValues)
     * para realizar una inserción segura, previniendo vulnerabilidades de inyección SQL.
     */
    fun insertarUsuario(usuario: String, correo: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("usuario", usuario)
            put("correo", correo)
            put("password", password)
        }
        // Retorna true si la inserción fue exitosa (ID de fila válido)
        return db.insert("usuarios", null, values) != -1L
    }

    /**
     * Este es un Select que ejecuta una consulta parametrizada (usando '?') para validar
     * la existencia de una combinación exacta de credenciales en el sistema.
     */
    fun validarUsuario(usuario: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ? AND password = ?", arrayOf(usuario, password))
        val existe = cursor.count > 0
        cursor.close() // Liberación del cursor de memoria para evitar fugas (guau)
        return existe
    }
    /**
     * Operación DQL (Select): Busca un usuario por su nombre y extrae su correo
     * registrado para inyectarlo en la memoria de sesión global.
     */
    fun obtenerCorreoUsuario(usuario: String): String {
        val db = this.readableDatabase
        var correo = "sin_correo@ejemplo.com" // Valor por defecto en caso de error

        // Ejecutamos la consulta buscando solo la columna 'correo'
        val cursor = db.rawQuery("SELECT correo FROM usuarios WHERE usuario = ?", arrayOf(usuario))

        // Si el cursor encuentra una fila, nos movemos a ella y extraemos el texto
        if (cursor.moveToFirst()) {
            correo = cursor.getString(0)
        }

        cursor.close() // Siempre cerramos el cursor para liberar memoria
        return correo
    }
}