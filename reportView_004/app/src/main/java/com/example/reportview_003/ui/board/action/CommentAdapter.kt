package com.example.reportview_003.ui.board.action

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.reportview_003.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommentAdapter(
    private val context: Context,
    private val comments: MutableList<MutableMap<String,Any>>
    ): BaseAdapter() {

    override fun getCount(): Int = comments.size

    override fun getItem(position: Int): MutableMap<String, Any> = comments[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.qna_detail_comment, parent, false)

        val createdAt: TextView = view.findViewById(R.id.qna_re_created_at)
        val contentTextView: TextView = view.findViewById(R.id.qna_re_content)

        val item = getItem(position)
        val createdAtText = item["qna_re_created_at"] as? String ?: "No Created At"
        val contentText = item["qna_re_content"] as? String ?: "No Content"

        // LocalDateTime 입력 포맷: "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        // 출력 포맷: "yyyy-MM-dd"
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val createdAtDateTime = LocalDateTime.parse(createdAtText, inputFormatter)
        createdAt.text = createdAtDateTime.format(outputFormatter)
        contentTextView.text = contentText

        return view
    }
}