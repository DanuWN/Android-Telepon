package com.example.telepon_ahmaddanu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

            val btnAddData: Button = findViewById(R.id.btnAddData)
            btnAddData.setOnClickListener {
                startActivity(Intent(this, CariNomor::class.java))
            }
                val btnViewData: Button = findViewById(R.id.btnViewData)
                btnViewData.setOnClickListener {
                    startActivity(Intent(this, BukuTelepon::class.java))
            }
        }
    }