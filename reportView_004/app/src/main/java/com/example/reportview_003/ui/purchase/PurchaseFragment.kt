package com.example.reportview_003.ui.purchase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.PurchaseAPI
import com.example.reportview_003.model.purchase.CartResponse
import com.example.reportview_003.ui.purchase.action.GetPurchaseCart
import com.example.reportview_003.ui.purchase.action.PurchaseCartAdapter

class PurchaseFragment : Fragment() {

    private lateinit var purchaseCartList: ListView
    private lateinit var purchaseCartTotalprice: TextView
    private lateinit var purchaseCartButton: Button
    var cartID: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.purchase_main, container, false)

        val app = requireActivity().application as App
        val purchaseAPI = app.retrofit.create(PurchaseAPI::class.java)

        purchaseCartList = view.findViewById(R.id.purchase_cart_list)
        purchaseCartTotalprice = view.findViewById(R.id.purchase_cart_totalprice)
        purchaseCartButton = view.findViewById(R.id.purchase_cart_button)

        val getPurchaseCart = GetPurchaseCart(requireContext(), purchaseAPI)
        // 디바이스에 저장된 book id 값
        getPurchaseCart.loadPurchaseCart(mutableListOf(1,2,3)) { response ->
            if (isAdded) {
                if (response != null){
                    updateUI(response)
                } else {
                    Log.e("PurchaseFragment", "Fragment is not attached to a context while loading data.")
                }
            }
        }

        return view
    }

    private fun updateUI(cartResponse: CartResponse) {
        purchaseCartTotalprice.text = cartResponse.total_price.toString()
        cartID = cartResponse.cart_id

        val app = requireActivity().application as App
        val purchaseAPI = app.retrofit.create(PurchaseAPI::class.java)

        val adapter = PurchaseCartAdapter(requireContext(), cartResponse.book_list, purchaseAPI)
        purchaseCartList.adapter = adapter
    }
}