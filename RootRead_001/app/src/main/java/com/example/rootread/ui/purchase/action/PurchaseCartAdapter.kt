package com.example.rootread.ui.purchase.action

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.rootread.App
import com.example.rootread.R
import com.example.rootread.api.PurchaseAPI
import com.example.rootread.model.purchase.CartResponse
import com.example.rootread.model.purchase.DeleteItemRequest
import com.example.rootread.model.purchase.EachCartItem
import com.example.rootread.utils.SessionManager
import java.text.NumberFormat
import java.util.Locale

class PurchaseCartAdapter(
    private val context: Context,
    private val cartResponse: CartResponse,
    private val onCartUpdated: (MutableList<EachCartItem>) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = cartResponse.purchaseCartDtoList.size

    override fun getItem(position: Int): EachCartItem {
        return cartResponse.purchaseCartDtoList[position]
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.purchase_cart_list, parent, false)

        val bookTitle: TextView = view.findViewById(R.id.book_title)
        val bookPrice: TextView = view.findViewById(R.id.book_price)
        val deleteButton: ImageView = view.findViewById(R.id.delete_button)

        val item = getItem(position)
        val userId = SessionManager.getUserID(context).toString()

        // Extract title and price from item
        val title = item.title as? String ?: "Unknown Title"
        val price = item.price
        val bookId = item.book_id

        // Set data to views
        bookTitle.text = title
        bookPrice.text = "${NumberFormat.getNumberInstance(Locale.US).format(price)} 원"

        deleteButton.setOnClickListener {
            if (bookId != -1L) {
                val app = context.applicationContext as App
                val purchaseAPI = app.retrofit.create(PurchaseAPI::class.java)

                val deleteItemRequest = DeleteItemRequest(userId, bookId)

                val doDelete = DoDelete(context, purchaseAPI)
                doDelete.doDelet(deleteItemRequest) { response ->
                    if (response != null) {
                        Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show()

                        cartResponse.purchaseCartDtoList.removeAt(position)
                        onCartUpdated(cartResponse.purchaseCartDtoList) // Notify the fragment
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
