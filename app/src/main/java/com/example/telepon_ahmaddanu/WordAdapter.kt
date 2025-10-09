package com.example.telepon_ahmaddanu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView

class WordAdapter {
    data class Word(val id: Int, val indonesia: String, val english: String, var isSelected: Boolean = false)

    class WordAdapter(private val context: Context, private val words: List<Word>) : BaseAdapter() {

        override fun getCount(): Int = words.size

        override fun getItem(position: Int): Word = words[position]

        override fun getItemId(position: Int): Long = words[position].id.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_word, parent, false)
            val word = getItem(position)

            val tvWord = view.findViewById<TextView>(R.id.tvWord)
            val cbSelect = view.findViewById<CheckBox>(R.id.cbSelect)

            tvWord.text = "${word.indonesia} - ${word.english}"
            cbSelect.isChecked = word.isSelected
            cbSelect.setOnCheckedChangeListener { _, isChecked ->
                word.isSelected = isChecked
            }

            return view
        }
    }
}