package com.example.telepon_ahmaddanu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView

data class Contact(
    val id: Int,
    val nama: String,
    val nomor: String,
    val alamat: String,
    val email: String,
    var isSelected: Boolean = false
)

class ContactAdapter(private val context: Context, private val contacts: List<Contact>) : BaseAdapter() {

    override fun getCount(): Int = contacts.size

    override fun getItem(position: Int): Contact = contacts[position]

    override fun getItemId(position: Int): Long = contacts[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
        val contact = getItem(position)

        val tvNama = view.findViewById<TextView>(R.id.tvNama)
        val tvNomor = view.findViewById<TextView>(R.id.tvNomor)
        val tvAlamat = view.findViewById<TextView>(R.id.tvAlamat)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val cbSelect = view.findViewById<CheckBox>(R.id.cbSelect)

        tvNama.text = contact.nama
        tvNomor.text = contact.nomor
        tvAlamat.text = contact.alamat
        tvEmail.text = contact.email

        cbSelect.isChecked = contact.isSelected
        cbSelect.setOnCheckedChangeListener { _, isChecked ->
            contact.isSelected = isChecked
        }

        return view
    }
}