package com.example.rootread.ui.board

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import com.example.rootread.R
import com.example.rootread.model.board.BoardResponse

class BoardAdapter(
    private val context: Context,
    private val data: BoardResponse,
    private val navController: NavController
) : BaseAdapter() {

    override fun getCount(): Int = data.qnaListDto.size

    override fun getItem(position: Int): MutableMap<String, Any> = data.qnaListDto[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.board_item, parent, false)

        val titleTextView: TextView = view.findViewById(R.id.board_item_title)
        val authorTextView: TextView = view.findViewById(R.id.board_item_author)
        val lockIcom: ImageView = view.findViewById(R.id.qna_lock)

        val item = getItem(position)
        val title = item["title"] as? String ?: "No Title"
        val author = item["user_id"] as? String ?: "Anonymous"

        titleTextView.text = title
        authorTextView.text = author

        // "secret" 값이 false이거나 존재하지 않으면 lockIcon을 숨김 처리, true이면 표시
        val secret = item["secret"] as? Boolean ?: false
        lockIcom.visibility = if (secret) View.VISIBLE else View.GONE

        return view
    }
}
