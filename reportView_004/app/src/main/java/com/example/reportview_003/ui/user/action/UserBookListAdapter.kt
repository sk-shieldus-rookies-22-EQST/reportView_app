package com.example.reportview_003.ui.user.action

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reportview_003.R

class UserBookListAdapter(
    private val context: Context,
    private val bookList: MutableList<MutableMap<String, Any>>,
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
            .load(item["book_img_path"] as? String ?: "")
            .placeholder(R.drawable.file_open_black)
            .into(holder.bookImage)

        // 클릭 이벤트 설정
        holder.itemView.setOnClickListener {
            val fileOpenAction = FileOpenAction(context)
            fileOpenAction.openFile(
                book_id = (item["book_id"] as? Number)?.toLong() ?: -1L
            )
        }
    }

    override fun getItemCount(): Int = bookList.size
}