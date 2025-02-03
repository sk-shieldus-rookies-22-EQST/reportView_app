package com.example.reportview_003.ui.user.action

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.navigation.NavController
import com.example.reportview_003.R
import java.text.NumberFormat
import java.util.Locale

class BuildPurchaseList(
    private val context: Context,
    private val data: MutableList<MutableMap<String, Any>>,
): BaseAdapter() {

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): MutableMap<String, Any> = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.user_purchase_item, parent, false)

        val item = getItem(position)

        val purchaseTitle: TextView = view.findViewById(R.id.user_purchase_title)
        val purchasePrice: TextView = view.findViewById(R.id.user_purchase_price)

        val price = when (val priceValue = item["price"]) {
            is Int -> priceValue
            is Double -> priceValue.toInt()
            is String -> priceValue.toIntOrNull() ?: 0
            else -> 0
        }

        // 데이터 설정
        purchaseTitle.text = item["title"] as? String ?: "Unknown Title"
        purchasePrice.text = "${NumberFormat.getNumberInstance(Locale.US).format(price)} 원"

        return view
    }
}