package com.example.reportview_003.ui.purchase.action

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.reportview_003.R
import com.example.reportview_003.api.PurchaseAPI
import com.example.reportview_003.model.purchase.deleteCartItemRequest
import com.example.reportview_003.repository.PurchaseRepository

class PurchaseCartAdapter(
    private val context: Context,
    private val bookList: MutableList<MutableMap<String, Any>>,
    private val purchaseApi: PurchaseAPI
) : BaseAdapter() {

    override fun getCount(): Int = bookList.size

    override fun getItem(position: Int): MutableMap<String, Any> = bookList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.purchase_cart_list, parent, false)

        val bookTitle: TextView = view.findViewById(R.id.book_title)
        val bookPrice: TextView = view.findViewById(R.id.book_price)
        val deleteButton: TextView = view.findViewById(R.id.delete_button)

        val item = getItem(position)

        // Extract title and price from item
        val title = item["title"] as? String ?: "Unknown Title"
        val price = item["price"] as? String ?: "Unknown Price"
        val bookId = item["id"] as? Int ?: -1

        // Set data to views
        bookTitle.text = title
        bookPrice.text = price

        deleteButton.setOnClickListener {
            if (bookId != -1) {
                deleteItemFromCart(deleteCartItemRequest(book_id = bookId), position)
            } else {
                Toast.makeText(context, "Invalid item ID", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun deleteItemFromCart(bookId: deleteCartItemRequest, position: Int) {
        val purchaseRepository = PurchaseRepository(purchaseApi)

        purchaseRepository.deleteFromCart(bookId) { success, error ->
            if (success) {
                // 리스트에서 아이템 제거
                bookList.removeAt(position)
                notifyDataSetChanged() // 리스트뷰 업데이트
                Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show()
            } else {
                error?.printStackTrace()
                Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
