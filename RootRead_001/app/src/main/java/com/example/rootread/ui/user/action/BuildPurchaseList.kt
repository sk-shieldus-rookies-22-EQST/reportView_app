package com.example.rootread.ui.user.action

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.navigation.NavController
import com.example.rootread.R
import java.text.NumberFormat
import java.util.Locale

class BuildPurchaseList(
    private val context: Context,
    private val data: MutableList<MutableMap<String, Any>>,
    private val navController: NavController
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

        view.setOnClickListener {
            val item = getItem(position)
            val bookId = (item["book_id"] as? Number)?.toLong() ?: -1L
            val bundle = Bundle().apply {
                putLong("book_id", bookId)
            }
            navController.navigate(R.id.action_userPurchaseFragment_to_bookDetailFragment, bundle)
        }

        return view
    }
}