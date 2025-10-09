package com.example.telepon_ahmaddanu

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CariNomor : AppCompatActivity() {
    private lateinit var wordAdapter: WordAdapter
    private val wordList = mutableListOf<WordAdapter.Word>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cari_nomor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etKata = findViewById<EditText>(R.id.etKata)
        val btnCari = findViewById<Button>(R.id.btnCari)
        val btnDeleteSelected = findViewById<Button>(R.id.btnDeleteSelected)
        val lvHasil = findViewById<ListView>(R.id.lvHasil)
        val dbHelper = DatabaseHelper(this)

        // Initialize ListView adapter
        wordAdapter = WordAdapter(this, wordList)
        lvHasil.adapter = wordAdapter

        // Load all words initially
        updateWordList(dbHelper, "")

        btnCari.setOnClickListener {
            val keyword = etKata.text.toString().trim()
            updateWordList(dbHelper, keyword)
        }

        btnDeleteSelected.setOnClickListener {
            val selectedIds = wordList.filter { it.isSelected }.map { it.id }
            if (selectedIds.isEmpty()) {
                Toast.makeText(this, "Pilih kata untuk dihapus", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rowsDeleted = dbHelper.deleteMultipleKata(selectedIds)
            if (rowsDeleted > 0) {
                Toast.makeText(this, "$rowsDeleted kata dihapus", Toast.LENGTH_SHORT).show()
                updateWordList(dbHelper, etKata.text.toString().trim())
            } else {
                Toast.makeText(this, "Gagal menghapus kata", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateWordList(dbHelper: DatabaseHelper, keyword: String) {
        wordList.clear()
        val cursor = if (keyword.isEmpty()) dbHelper.getAllKata() else dbHelper.searchKata(keyword)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val indonesia = cursor.getString(cursor.getColumnIndexOrThrow("indonesia"))
            val english = cursor.getString(cursor.getColumnIndexOrThrow("english"))
            wordList.add(Word(id, indonesia, english))
        }
        cursor.close()
        wordAdapter.notifyDataSetChanged()
        if (wordList.isEmpty() && keyword.isNotEmpty()) {
            Toast.makeText(this, "Tidak ditemukan kata yang cocok", Toast.LENGTH_SHORT).show()
        }
    }
}