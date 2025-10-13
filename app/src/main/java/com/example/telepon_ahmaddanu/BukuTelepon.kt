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

class BukuTelepon : AppCompatActivity() {
    private lateinit var contactAdapter: ContactAdapter
    private val contactList = mutableListOf<Contact>()
    private var editingContactId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_buku_telepon)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etNama = findViewById<EditText>(R.id.etNama)
        val etNo = findViewById<EditText>(R.id.etNo)
        val etAlamat = findViewById<EditText>(R.id.etAlamat)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnHitung = findViewById<Button>(R.id.btnHitung)
        val btnEdit = findViewById<Button>(R.id.btnEdit)
        val btnDeleteSelected = findViewById<Button>(R.id.btnDeleteSelected)
        val lvHasil = findViewById<ListView>(R.id.lvHasil)
        val dbHelper = DatabaseHelper(this)

        // Initialize ListView adapter
        contactAdapter = ContactAdapter(this, contactList)
        lvHasil.adapter = contactAdapter

        // Load all contacts
        loadContacts(dbHelper)

        // Add Contact
        btnHitung.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val nomor = etNo.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()
            val email = etEmail.text.toString().trim()

            if (nama.isEmpty()) {
                Toast.makeText(this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nomor.isEmpty()) {
                Toast.makeText(this, "Nomor tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = dbHelper.insertTelepon(nama, nomor, alamat, email)
            if (id > 0) {
                Toast.makeText(this, "Kontak berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                clearForm(etNama, etNo, etAlamat, etEmail)
                loadContacts(dbHelper)
            } else {
                Toast.makeText(this, "Gagal menambahkan kontak!", Toast.LENGTH_SHORT).show()
            }
        }

        // Edit Contact
        btnEdit.setOnClickListener {
            if (editingContactId == null) {
                Toast.makeText(this, "Pilih kontak untuk diedit!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nama = etNama.text.toString().trim()
            val nomor = etNo.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()
            val email = etEmail.text.toString().trim()

            if (nama.isEmpty() || nomor.isEmpty()) {
                Toast.makeText(this, "Nama dan Nomor harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rows = dbHelper.updateContact(editingContactId!!, nama, nomor, alamat, email)
            if (rows > 0) {
                Toast.makeText(this, "Kontak berhasil diupdate!", Toast.LENGTH_SHORT).show()
                clearForm(etNama, etNo, etAlamat, etEmail)
                editingContactId = null
                btnHitung.text = "Tambah Kontak"
                loadContacts(dbHelper)
            } else {
                Toast.makeText(this, "Gagal mengupdate kontak!", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete Selected Contacts
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
                        loadContacts(dbHelper)
                    }
                }
                .setNegativeButton("Tidak", null)
                .show()
        }

        // Click item to edit
        lvHasil.setOnItemClickListener { _, _, position, _ ->
            val contact = contactList[position]
            etNama.setText(contact.nama)
            etNo.setText(contact.nomor)
            etAlamat.setText(contact.alamat)
            etEmail.setText(contact.email)
            editingContactId = contact.id
            btnHitung.text = "Update Kontak"
            Toast.makeText(this, "Edit mode: ${contact.nama}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadContacts(dbHelper: DatabaseHelper) {
        contactList.clear()
        val cursor = dbHelper.getAllContact()
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
    }

    private fun clearForm(vararg editTexts: EditText) {
        editTexts.forEach { it.text.clear() }
    }
}