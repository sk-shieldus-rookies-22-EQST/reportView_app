package f0.c.rootread.ui.purchase.action

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import f0.c.rootread.R
import f0.c.rootread.api.PurchaseAPI
import f0.c.rootread.model.purchase.EachCartItem
import java.text.NumberFormat
import java.util.Locale

class PurchaseProcessAdapter(
    private val context: Context,
    private val cartList: ArrayList<EachCartItem>,
    private val purchaseApi: PurchaseAPI
): BaseAdapter() {

    override fun getCount(): Int = cartList.size

    override fun getItem(position: Int): EachCartItem = cartList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.purchase_process_item, parent, false)

        val bookTitle = view.findViewById<TextView>(R.id.purchase_process_item_title)
        val bookPrice = view.findViewById<TextView>(R.id.purchase_process_item_price)

        val item = getItem(position)

        val price = item.price

        bookTitle.text = item.title as? String ?: "Unknown"
        bookPrice.text = "${NumberFormat.getNumberInstance(Locale.US).format(price as? Int ?: 0)} 원"

        return view
    }


}