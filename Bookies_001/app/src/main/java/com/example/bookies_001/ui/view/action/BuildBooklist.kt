package com.example.bookies_001.ui.view.action

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
import com.example.bookies_001.App
import com.example.bookies_001.R
import com.example.bookies_001.api.PurchaseAPI
import com.example.bookies_001.model.purchase.CartGetItemRequest
import com.example.bookies_001.ui.purchase.action.InsertCartItem
import com.example.bookies_001.utils.SessionManager
import java.text.NumberFormat
import java.util.Locale

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
        val price = when (val priceValue = item["price"]) {
            is Int -> priceValue
            is Double -> priceValue.toInt()
            is String -> priceValue.toIntOrNull() ?: 0
            else -> 0
        }
        val writer = item["writer"] as? String ?: "unknown writer"
        // 현재 받아오는 값은 상대경로 이미지로 도메인 없이 경로만을 가지고 있음
        // http://도메인:포트/이미지 경로
        // http://도메인:포트/ <-- 하드코딩으로 집어 넣을 예정
//        val img = item["book_img_path"] as? String ?: ""
        val img = "https://dahaezlge.kro.kr:30303/images/test.jpg"

        holder.bookTitle.text = title
        holder.bookAuthor.text = writer
        holder.bookPrice.text = "${NumberFormat.getNumberInstance(Locale.US).format(price)} 원"

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
                val bookId = when (val id = item["book_id"]) {
                    is Long -> id // 이미 Int인 경우
                    is Double -> id.toLong() // Double인 경우 Int로 변환
                    is String -> id.toLongOrNull() ?: -1 // String인 경우 안전하게 Int로 변환
                    else -> -1 // 잘못된 형식인 경우
                }
                val userId = SessionManager.getUserID(context).toString()
                val cartGetItemRequest = CartGetItemRequest(
                    user_id = userId,
                    book_id = bookId
                )
                val app = context.applicationContext as App
                val purchaseAPI = app.retrofit.create(PurchaseAPI::class.java)
                val insertCartItem = InsertCartItem(context, purchaseAPI)
                insertCartItem.insertCartItem(cartGetItemRequest) { response ->
                    if (response != null) {
                        if (response.status) {
                            Toast.makeText(context, "장바구니에 담겼습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "장바구니 담기 실패", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "장바구니 담기 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 아이템 클릭 리스너: BookDetailFragment로 이동하며 book_id 전달
        view.setOnClickListener {
            val item = getItem(position)
            val bookId = (item["book_id"] as? Number)?.toLong() ?: -1L
            val bundle = Bundle().apply {
                putLong("book_id", bookId)
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
