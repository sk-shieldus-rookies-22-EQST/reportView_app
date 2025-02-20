package com.example.rootread.ui.purchase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rootread.App
import com.example.rootread.R
import com.example.rootread.api.PurchaseAPI
import com.example.rootread.model.purchase.CartRequest
import com.example.rootread.model.purchase.CartResponse
import com.example.rootread.ui.purchase.action.GetPurchaseCart
import com.example.rootread.ui.purchase.action.PurchaseCartAdapter
import com.example.rootread.utils.SessionManager
import java.text.NumberFormat
import java.util.Locale

class PurchaseFragment : Fragment() {

    private lateinit var purchaseCartList: ListView
    private lateinit var purchaseCartTotalprice: TextView
    private lateinit var purchaseCartButton: Button
    private var cartId: Long = -1L
    private var cartList: MutableList<MutableMap<String, Any>>? = mutableListOf()
    private var totalPrice: Int = 0

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

        val cartreq = CartRequest(
            user_id = SessionManager.getUserID(requireContext())
        )

        // 사용자 id 값으로 카트에 있는 데이터 조회
        getPurchaseCart.loadPurchaseCart(cartreq) { response ->
            if (isAdded) {
                if (response != null){
                    cartList = response.purchaseCartDtoList
                    cartId = cartList?.firstOrNull()
                        ?.takeIf { "cart_id" in it }
                        ?.get("cart_id")
                        ?.toString()
                        ?.toLongOrNull() ?: -1L

                    updateUI(response)

                } else {
                    Log.e("PurchaseFragment", "Fragment is not attached to a context while loading data.")
                }
            }
        }

        purchaseCartButton.setOnClickListener {
            // 불러온 값을 다음 단계로 전달
            val bundle = Bundle()
            bundle.putLong("cart_id", cartId)
            bundle.putSerializable("cart_list", cartList as ArrayList<MutableMap<String, Any>>)
            bundle.putInt("total_price", totalPrice)

            findNavController().navigate(R.id.action_purchaseFragment_to_purchaseProcessFragment, bundle)
        }

        return view
    }

    private fun updateTotalPrice() {
        totalPrice = cartList?.sumOf {
            when (val priceValue = it["price"]) {
                is Int -> priceValue
                is Double -> priceValue.toInt()
                is String -> priceValue.toIntOrNull() ?: 0
                else -> 0
            }
        } ?: 0
        purchaseCartTotalprice.text = "합계 : ${NumberFormat.getNumberInstance(Locale.US).format(totalPrice)} 원"
    }



    private fun updateUI(cartResponse: CartResponse) {
        updateTotalPrice()

        val adapter = PurchaseCartAdapter(requireContext(), cartResponse) { updatedCart ->
            cartList = updatedCart
            updateTotalPrice()
        }
        purchaseCartList.adapter = adapter
    }
}