package com.example.telepon_ahmaddanu

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CariNomor : AppCompatActivity() {
    private lateinit var contactAdapter: ContactAdapter
    private val contactList = mutableListOf<Contact>()

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
        contactAdapter = ContactAdapter(this, contactList)
        lvHasil.adapter = contactAdapter

        // Load all contacts initially
        updateContactList(dbHelper, "")

        btnCari.setOnClickListener {
            val keyword = etKata.text.toString().trim()
            updateContactList(dbHelper, keyword)
        }

        btnDeleteSelected.setOnClickListener {
            val selectedIds = contactList.filter { it.isSelected }.map { it.id }
            if (selectedIds.isEmpty()) {
                Toast.makeText(this, "Pilih kontak untuk dihapus!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Hapus Kontak")
                .setMessage("Yakin ingin menghapus ${selectedIds.size} kontak?")
                .setPositiveButton("Ya") { _, _ ->
                    val rowsDeleted = dbHelper.deleteMultipleContact(selectedIds)
                    if (rowsDeleted > 0) {
                        Toast.makeText(this, "$rowsDeleted kontak dihapus", Toast.LENGTH_SHORT).show()
                        updateContactList(dbHelper, etKata.text.toString().trim())
                    } else {
                        Toast.makeText(this, "Gagal menghapus kontak!", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Tidak", null)
                .show()
        }

        // Long click to view contact details
        lvHasil.setOnItemLongClickListener { _, _, position, _ ->
            val contact = contactList[position]
            AlertDialog.Builder(this)
                .setTitle(contact.nama)
                .setMessage(
                    "Nomor: ${contact.nomor}\n" +
                            "Alamat: ${contact.alamat}\n" +
                            "Email: ${contact.email}"
                )
                .setPositiveButton("OK", null)
                .show()
            true
        }
    }

    private fun updateContactList(dbHelper: DatabaseHelper, keyword: String) {
        contactList.clear()
        val cursor = if (keyword.isEmpty()) {
            dbHelper.getAllContact()
        } else {
            dbHelper.searchContact(keyword)
        }

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID))
            val nama = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAMA))
            val nomor = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOMOR))
            val alamat = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ALAMAT))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL))
            contactList.add(Contact(id, nama, nomor, alamat, email))
        }
        cursor.close()
        contactAdapter.notifyDataSetChanged()

        if (contactList.isEmpty() && keyword.isNotEmpty()) {
            Toast.makeText(this, "Tidak ditemukan kontak yang cocok", Toast.LENGTH_SHORT).show()
        }
    }
}