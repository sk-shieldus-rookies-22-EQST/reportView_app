package com.example.reportview_003.ui.board

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.reportview_003.R

class BoardAdapter(
    private val context: Context,
    private val data: List<MutableMap<String, Any>>
) : BaseAdapter() {

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): MutableMap<String, Any> = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.board_item, parent, false)

        val titleTextView: TextView = view.findViewById(R.id.board_item_title)
        val authorTextView: TextView = view.findViewById(R.id.board_item_author)

        val item = getItem(position)
        val title = item["title"] as? String ?: "No Title"
        val author = item["author"] as? String ?: "Anonymous"

        titleTextView.text = title
        authorTextView.text = author

        return view
    }
}
