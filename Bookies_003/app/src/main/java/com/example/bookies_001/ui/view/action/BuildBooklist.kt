package com.example.bookies_001.ui.view.action

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookies_001.App
import com.example.bookies_001.R
import com.example.bookies_001.api.PurchaseAPI
import com.example.bookies_001.model.purchase.CartGetItemRequest
import com.example.bookies_001.model.view.EachBook
import com.example.bookies_001.ui.purchase.action.InsertCartItem
import com.example.bookies_001.utils.SessionManager
import java.text.NumberFormat
import java.util.Locale

class BuildBooklist(
    private val context: Context,
    private val data: MutableList<EachBook>,
    private val navController: NavController
) : RecyclerView.Adapter<BuildBooklist.ViewHolder>() {

    // ViewHolder 패턴 적용 (뷰 재사용 최적화)
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.main_book_title)
        val writer: TextView = view.findViewById(R.id.main_book_writer)
        val price: TextView = view.findViewById(R.id.main_book_price)
        val img: ImageView = view.findViewById(R.id.book_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.title.text = item.title
        holder.price.text = formatPrice(item.price)
        holder.writer.text = item.writer

        holder.itemView.setOnClickListener {
            navigateToDetail(item.book_id)
        }

        val imgUrl = "https://3.35.84.46" + item.book_img_path
        Glide.with(context)
            .load(imgUrl)
            .placeholder(R.drawable.root_read_logo)
            .into(holder.img)
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

    override fun getItemCount(): Int = data.size

}
