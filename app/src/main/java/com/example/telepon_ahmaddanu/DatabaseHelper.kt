package com.example.telepon_ahmaddanu

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "bukutelepon", null, 1) {

    companion object {
        const val TABLE_TELEPON = "bukutelepon"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE $TABLE_TELEPON (id INTEGER PRIMARY KEY AUTOINCREMENT, nama String, nomor_telepon varchar(100), alamat String, email String)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TELEPON")
        onCreate(db)
    }

    // CREATE
    fun insertTelepon(nama: String, notelp: String, alamat: String, email: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nama", nama)
            put("nomor telepon", notelp)
            put("alamat", alamat)
            put("email", email)
        }
        val id = db.insert(TABLE_TELEPON, null, values)
        db.close()
        return id
    }

    // READ
    fun getAllContact(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_TELEPON, null, null, null, null, null, "id DESC")
    }

    // UPDATE
    fun updateContact(id: Int, nama: String, notelp: String, alamat: String, email: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nama", nama)
            put("nomor telepon", notelp)
            put("alamat", alamat)
            put("email", email)
        }
        val rows = db.update(TABLE_TELEPON, values, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    //SEARCH
    fun searchKata(keyword: String): Cursor {
        val db = readableDatabase
        return db.query(
            TABLE_TELEPON,
            null,
            "nama LIKE ? OR nomor telepon LIKE ? OR alamat LIKE ? OR email LIKE ?",
            arrayOf("%$keyword%", "%$keyword%"),
            null,
            null,
            null
        )
    }

    fun getAllKata(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_TELEPON, null, null, null, null, null, "id DESC")
    }

    // DELETE
    fun deleteContact(id: Int): Int {
        val db = writableDatabase
        val rows = db.delete(TABLE_TELEPON, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    fun deleteMultipleKata(ids: List<Int>): Int {
        val db = writableDatabase
        val idsString = ids.joinToString(",")
        val rows = db.delete(TABLE_TELEPON, "id IN ($idsString)", null)
        db.close()
        return rows
    }
}