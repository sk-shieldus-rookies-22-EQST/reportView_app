package com.example.reportview_003.ui.view.action

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.example.reportview_003.R

/*
* 리스트 목록을 출력해주는 Adapter
* 현재 로컬에 저장되어 있는지 정보와 제목으로 리스트 뷰를 꾸며서 전달해줌
* */

class BuildBooklist(
    private val context: Context,
    private val data : List<Map<String, Any>>,
    private val navController: NavController
): BaseAdapter() {

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): Map<String, Any> = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        // View Holder 패턴 적용
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            holder = ViewHolder(
                view.findViewById(R.id.book_title),
                view.findViewById(R.id.book_author),
                view.findViewById(R.id.book_price),
                view.findViewById(R.id.book_img)
            )
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val item = getItem(position)

        val title = item["title"] as? String ?: "unknown Title"
        val price = item["price"] as? String ?: "unknown Price"
        val writer = item["writer"] as? String ?: "unknown writer"
        val img = item["book_img_path"] as? String ?: ""

        holder.bookTitle.text = title
        holder.bookAuthor.text = writer
        holder.bookPrice.text = price

        Glide.with(context)
            .load(img)
            .placeholder(R.drawable.download_black)
            .into(holder.bookImg)

        // 클릭 리스너에 NavController 전달
        view.setOnClickListener {
            true
        }

        return view
    }

    // View Holder 클래스
    private data class ViewHolder(
        val bookTitle: TextView,
        val bookAuthor: TextView,
        val bookPrice: TextView,
        val bookImg: ImageView
    )
}