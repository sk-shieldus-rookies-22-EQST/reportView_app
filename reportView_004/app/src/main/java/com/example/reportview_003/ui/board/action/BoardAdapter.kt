package com.example.reportview_003.ui.board

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.navigation.NavController
import com.example.reportview_003.R
import com.example.reportview_003.model.board.BoardResponse

class BoardAdapter(
    private val context: Context,
    private val data: BoardResponse,
    private val navController: NavController
) : BaseAdapter() {

    override fun getCount(): Int = data.qna.size

    override fun getItem(position: Int): MutableMap<String, Any> = data.qna[position]

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
