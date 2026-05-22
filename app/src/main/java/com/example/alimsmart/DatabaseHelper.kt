package com.example.alimsmart

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "AlimSmartDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        val createTable = """
            CREATE TABLE usuarios(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                correo TEXT,
                password TEXT
            )
        """.trimIndent()

        db.execSQL(createTable)

        // USUARIO DE PRUEBA
        val values = ContentValues()
        values.put("correo", "admin")
        values.put("password", "1234")

        db.insert("usuarios", null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    fun validarUsuario(correo: String, password: String): Boolean {

        val db = readableDatabase

        val query = """
            SELECT * FROM usuarios
            WHERE correo = ? AND password = ?
        """

        val cursor = db.rawQuery(query, arrayOf(correo, password))

        val existe = cursor.count > 0

        cursor.close()

        return existe
    }

    fun insertarUsuario(correo: String, password: String): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put("correo", correo)
        values.put("password", password)

        val resultado = db.insert("usuarios", null, values)

        return resultado != -1L
    }
}