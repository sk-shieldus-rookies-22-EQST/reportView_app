package com.example.reportview_003.ui.purchase.action

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.reportview_003.R
import com.example.reportview_003.api.PurchaseAPI
import java.util.ArrayList

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
        bookTitle.text = item["title"] as? String ?: "Unknown"
        bookPrice.text = "₩ ${item["price"] as? String ?: "0"}"

        return view
    }


}