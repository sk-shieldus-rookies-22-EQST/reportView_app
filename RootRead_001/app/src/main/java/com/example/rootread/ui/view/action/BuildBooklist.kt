package com.example.rootread.ui.view.action

import android.annotation.SuppressLint
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
import com.example.rootread.App
import com.example.rootread.R
import com.example.rootread.api.PurchaseAPI
import com.example.rootread.model.purchase.CartGetItemRequest
import com.example.rootread.model.view.EachBook
import com.example.rootread.ui.purchase.action.InsertCartItem
import com.example.rootread.utils.SessionManager
import java.text.NumberFormat
import java.util.Locale

class BuildBooklist(
    private val context: Context,
    private val data: MutableList<EachBook>,
    private val navController: NavController
) : BaseAdapter() {

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): EachBook = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val item = getItem(position)

        // 이미지 경로 처리
        val baseUrl = "https://3.35.84.46"
        val imageUrl = if (item.book_img_path.isNullOrEmpty()) {
            "" // 기본 이미지 사용
        } else {
            baseUrl + item.book_img_path
        }

        // UI 업데이트
        holder.title.text = item.title
        holder.price.text = formatPrice(item.price)
        holder.writer.text = item.writer

        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.root_read_logo)
            .into(holder.img)

        // 장바구니 버튼 클릭 리스너
        holder.cart.setOnClickListener {
            handleCartClick(item)
        }

        // 아이템 클릭 리스너: BookDetailFragment로 이동하며 book_id 전달
        view.setOnClickListener {
            navigateToDetail(item.book_id)
        }

        return view
    }

    // ViewHolder 패턴 적용 (뷰 재사용 최적화)
    private class ViewHolder(view: View) {
        val title: TextView = view.findViewById(R.id.book_title)
        val price: TextView = view.findViewById(R.id.book_price)
        val writer: TextView = view.findViewById(R.id.book_author)
        val img: ImageView = view.findViewById(R.id.book_img)
        val cart: ImageView = view.findViewById(R.id.book_cart)
    }

    // 가격을 통화 형식으로 변환하는 함수
    private fun formatPrice(price: Int): String {
        return NumberFormat.getNumberInstance(Locale.KOREA).format(price) + " 원"
    }

    // 장바구니 추가 처리
    private fun handleCartClick(item: EachBook) {
        if (!SessionManager.isLoggedIn(context)) {
            Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            navController.navigate(R.id.listFragment) // 로그인 화면으로 이동
            return
        }

        val userId = SessionManager.getUserID(context).toString()
        val cartRequest = CartGetItemRequest(user_id = userId, book_id = item.book_id)

        val app = context.applicationContext as App
        val purchaseAPI = app.retrofit.create(PurchaseAPI::class.java)
        val insertCartItem = InsertCartItem(context, purchaseAPI)

        insertCartItem.insertCartItem(cartRequest) { response ->
            val message = if (response?.status == true) "장바구니에 담겼습니다." else "장바구니 담기 실패"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // 상세 페이지로 이동
    private fun navigateToDetail(bookId: Long) {
        val bundle = Bundle().apply { putLong("book_id", bookId) }
        navController.navigate(R.id.action_listFragment_to_bookDetailFragment, bundle)
    }
}
