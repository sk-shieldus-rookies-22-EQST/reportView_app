package com.example.rootread.ui.purchase.action

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.rootread.R
import com.example.rootread.api.PurchaseAPI
import java.text.NumberFormat
import java.util.Locale

class PurchaseProcessAdapter(
    private val context: Context,
    private val cartList: List<Map<String, Any>>,
    private val purchaseApi: PurchaseAPI
): BaseAdapter() {

    override fun getCount(): Int = cartList.size

    override fun getItem(position: Int): Map<String, Any> = cartList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.purchase_process_item, parent, false)

        val bookTitle = view.findViewById<TextView>(R.id.purchase_process_item_title)
        val bookPrice = view.findViewById<TextView>(R.id.purchase_process_item_price)

        val item = getItem(position)

        val price = when (val priceValue = item["price"]) {
            is Int -> priceValue
            is Double -> priceValue.toInt()
            is String -> priceValue.toIntOrNull() ?: 0
            else -> 0
        }

        bookTitle.text = item["title"] as? String ?: "Unknown"
        bookPrice.text = "${NumberFormat.getNumberInstance(Locale.US).format(price as? Int ?: 0)} 원"

        return view
    }


}