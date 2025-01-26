package com.example.reportview_003.ui.view.action

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.example.reportview_003.R
import com.example.reportview_003.model.view.ViewbooklistResponse
import com.example.reportview_003.utils.SessionManager

/*
* 리스트 목록을 출력해주는 Adapter
* 현재 로컬에 저장되어 있는지 정보와 제목으로 리스트 뷰를 꾸며서 전달해줌
* */

class BuildBooklist(
    private val context: Context,
    private val data: List<Map<String, Any>>,
    private val navController: NavController
) : BaseAdapter() {

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
                view.findViewById(R.id.book_img),
                view.findViewById(R.id.book_cart),
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

        // 장바구니 클릭 리스너 설정
        holder.bookCart.setOnClickListener {
            if (!SessionManager.isLoggedIn(context)) {
                Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.listFragment) // 루트 페이지로 이동
            } else {
                // 장바구니 동작 구현
                Toast.makeText(context, "장바구니에 담겼습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 아이템 클릭 리스너: BookDetailFragment로 이동하며 book_id 전달
        view.setOnClickListener {
            val item = getItem(position)
            val bookId = (item["book_id"] as? Double)?.toInt() ?: -1
            val bundle = Bundle().apply {
                putInt("book_id", bookId)
            }
            navController.navigate(R.id.action_listFragment_to_bookDetailFragment, bundle)
        }

        return view
    }

    // View Holder 클래스
    private data class ViewHolder(
        val bookTitle: TextView,
        val bookAuthor: TextView,
        val bookPrice: TextView,
        val bookImg: ImageView,
        val bookCart: ImageView,
    )
}
