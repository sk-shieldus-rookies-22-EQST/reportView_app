package com.example.reportview_003.ui.purchase.action

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.PurchaseAPI
import com.example.reportview_003.model.purchase.CartResponse
import com.example.reportview_003.model.purchase.DeleteItemRequest

class PurchaseCartAdapter(
    private val context: Context,
    private val cartResponse: CartResponse,
    private val purchaseApi: PurchaseAPI,
    private val onCartUpdated: (MutableList<MutableMap<String, Any>>) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = cartResponse.book_list.size

    override fun getItem(position: Int): MutableMap<String, Any> {
        return cartResponse.book_list[position]
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.purchase_cart_list, parent, false)

        val bookTitle: TextView = view.findViewById(R.id.book_title)
        val bookPrice: TextView = view.findViewById(R.id.book_price)
        val deleteButton: ImageView = view.findViewById(R.id.delete_button)

        val item = getItem(position)
        val cartId = cartResponse.cart_id as? Int ?: -1

        // Extract title and price from item
        val title = item["title"] as? String ?: "Unknown Title"
        val price = item["price"] as? String ?: "Unknown Price"
        val bookId = when (val id = item["book_id"]) {
            is Int -> id // 이미 Int인 경우
            is Double -> id.toInt() // Double인 경우 Int로 변환
            is String -> id.toIntOrNull() ?: -1 // String인 경우 안전하게 Int로 변환
            else -> -1 // 잘못된 형식인 경우
        }

        // Set data to views
        bookTitle.text = title
        bookPrice.text = price

        deleteButton.setOnClickListener {
            if (bookId != -1) {
                val app = context.applicationContext as App
                val purchaseAPI = app.retrofit.create(PurchaseAPI::class.java)

                val deleteItemRequest = DeleteItemRequest(cartId, bookId)

                val doDelete = DoDelete(context, purchaseAPI)
                doDelete.doDelet(deleteItemRequest) { response ->
                    if (response != null) {
                        Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show()

                        cartResponse.book_list.removeAt(position)
                        onCartUpdated(cartResponse.book_list) // Notify the fragment
                        notifyDataSetChanged()
                    } else {
                        Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Invalid item ${bookId}", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

}
