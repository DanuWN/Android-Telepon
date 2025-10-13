package com.example.telepon_ahmaddanu

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "bukutelepon.db", null, 1) {

    companion object {
        const val TABLE_TELEPON = "bukutelepon"
        const val COL_ID = "id"
        const val COL_NAMA = "nama"
        const val COL_NOMOR = "nomor_telepon"
        const val COL_ALAMAT = "alamat"
        const val COL_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            """CREATE TABLE $TABLE_TELEPON (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COL_NAMA TEXT, 
                $COL_NOMOR TEXT, 
                $COL_ALAMAT TEXT, 
                $COL_EMAIL TEXT
            )"""
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
            put(COL_NAMA, nama)
            put(COL_NOMOR, notelp)
            put(COL_ALAMAT, alamat)
            put(COL_EMAIL, email)
        }
        val id = db.insert(TABLE_TELEPON, null, values)
        db.close()
        return id
    }

    // READ
    fun getAllContact(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_TELEPON, null, null, null, null, null, "$COL_NAMA ASC")
    }

    // UPDATE
    fun updateContact(id: Int, nama: String, notelp: String, alamat: String, email: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NAMA, nama)
            put(COL_NOMOR, notelp)
            put(COL_ALAMAT, alamat)
            put(COL_EMAIL, email)
        }
        val rows = db.update(TABLE_TELEPON, values, "$COL_ID = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    // SEARCH
    fun searchContact(keyword: String): Cursor {
        val db = readableDatabase
        return db.query(
            TABLE_TELEPON,
            null,
            "$COL_NAMA LIKE ? OR $COL_NOMOR LIKE ? OR $COL_ALAMAT LIKE ? OR $COL_EMAIL LIKE ?",
            arrayOf("%$keyword%", "%$keyword%", "%$keyword%", "%$keyword%"),
            null,
            null,
            "$COL_NAMA ASC"
        )
    }

    // DELETE
    fun deleteContact(id: Int): Int {
        val db = writableDatabase
        val rows = db.delete(TABLE_TELEPON, "$COL_ID = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    fun deleteMultipleContact(ids: List<Int>): Int {
        val db = writableDatabase
        val idsString = ids.joinToString(",")
        val rows = db.delete(TABLE_TELEPON, "$COL_ID IN ($idsString)", null)
        db.close()
        return rows
    }
}