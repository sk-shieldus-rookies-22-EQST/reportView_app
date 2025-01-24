package com.example.reportview_003.ui.user.action

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reportview_003.R

class UserBookListAdapter(
    private val context: Context,
    private val bookList: List<Map<String, Any>>
) : RecyclerView.Adapter<UserBookListAdapter.UserBookViewHolder>() {

    inner class UserBookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookImage: ImageView = view.findViewById(R.id.user_book_img)
        val bookTitle: TextView = view.findViewById(R.id.user_book_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserBookViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_book_list, parent, false)
        return UserBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserBookViewHolder, position: Int) {
        val item = bookList[position]
        holder.bookTitle.text = item["title"] as? String ?: "Unknown Title"

        // 이미지를 URL에서 불러오려면 Glide 또는 Picasso 사용
        Glide.with(context)
            .load(item["imageUrl"] as? String ?: "")
            .placeholder(R.drawable.file_open_black)
            .into(holder.bookImage)
    }

    override fun getItemCount(): Int = bookList.size
}