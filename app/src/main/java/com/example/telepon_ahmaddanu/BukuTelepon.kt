package com.example.telepon_ahmaddanu

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BukuTelepon : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_buku_telepon)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etSisi = findViewById<EditText>(R.id.etSisi)
        val btnHitung = findViewById<Button>(R.id.btnHitung)
        val tvHasil = findViewById<TextView>(R.id.tvHasil)
        val dbHelper = DatabaseHelper(this)

        btnHitung.setOnClickListener {
            val sisiText = etSisi.text.toString()

            if (sisiText.isEmpty()) {
                Toast.makeText(this, "Masukkan sisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sisi = sisiText.toDoubleOrNull()

            if (sisi == null || sisi <= 0) {
                Toast.makeText(this, "Sisi harus angka positif!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val luas = sisi * sisi
            tvHasil.text = "Luas Persegi: $luas"
            dbHelper.insertPersegi(sisi, luas)
            Toast.makeText(this, "Perhitungan disimpan!", Toast.LENGTH_SHORT).show()
        }
    }
}