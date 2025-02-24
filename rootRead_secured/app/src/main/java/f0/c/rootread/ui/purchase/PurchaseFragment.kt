package f0.c.rootread.ui.purchase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import f0.c.rootread.App
import f0.c.rootread.R
import f0.c.rootread.api.PurchaseAPI
import f0.c.rootread.model.purchase.CartRequest
import f0.c.rootread.model.purchase.EachCartItem
import f0.c.rootread.ui.purchase.action.GetPurchaseCart
import f0.c.rootread.ui.purchase.action.PurchaseCartAdapter
import f0.c.rootread.utils.SessionManager
import java.text.NumberFormat
import java.util.Locale

class PurchaseFragment : Fragment() {

    private lateinit var purchaseCartList: ListView
    private lateinit var purchaseCartTotalprice: TextView
    private lateinit var purchaseCartButton: Button
    private var cartId: Long = -1L
    private var cartList: MutableList<EachCartItem>? = mutableListOf()
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

        // 🛠 예외 처리 추가: 사용자 ID가 null인 경우
        if (cartreq.user_id.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.listFragment) // 로그인 화면으로 이동
            return view
        }

        // 🛠 예외 처리 추가: API 응답이 null인 경우 방어 코드
        getPurchaseCart.loadPurchaseCart(cartreq) { response ->
            if (isAdded) {
                if (response?.purchaseCartDtoList.isNullOrEmpty()) {
                    Log.w("PurchaseFragment", "장바구니가 비어 있습니다.")
                    Toast.makeText(requireContext(), "장바구니가 비어 있습니다.", Toast.LENGTH_SHORT).show()
                    return@loadPurchaseCart
                }

                // ✅ 첫 번째 아이템을 가져오되, 예외 방지
                cartList = response?.purchaseCartDtoList ?: mutableListOf()
                cartId = cartList?.firstOrNull()?.cart_id ?: -1L

                if (response != null) {
                    updateUI(response)
                }
            } else {
                Log.e("PurchaseFragment", "Fragment is not attached to a context while loading data.")
            }
        }

        purchaseCartButton.setOnClickListener {
            // ✅ 예외 처리: 장바구니가 비어 있을 경우 결제 진행 X
            if (cartList.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "장바구니가 비어 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ 불러온 값을 다음 단계로 전달
            val bundle = Bundle()
            bundle.putLong("cart_id", cartId)
            bundle.putSerializable("cart_list", cartList as ArrayList<MutableMap<String, Any>>)
            bundle.putInt("total_price", totalPrice)

            findNavController().navigate(R.id.action_purchaseFragment_to_purchaseProcessFragment, bundle)
        }

        return view
    }

    private fun updateTotalPrice() {
        // ✅ 예외 처리: cartList가 null이거나 비어 있을 경우 0으로 설정
        totalPrice = cartList?.takeIf { it.isNotEmpty() }?.sumOf { it.price } ?: 0
        purchaseCartTotalprice.text = "합계 : ${NumberFormat.getNumberInstance(Locale.US).format(totalPrice)} 원"
    }

    private fun updateUI(cartResponse: f0.c.rootread.model.purchase.CartResponse) {
        updateTotalPrice()

        val adapter = PurchaseCartAdapter(requireContext(), cartResponse) { updatedCart ->
            cartList = updatedCart
            updateTotalPrice()
        }
        purchaseCartList.adapter = adapter
    }
}
