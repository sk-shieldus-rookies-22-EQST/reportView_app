package com.example.bookies_001.ui.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.bookies_001.ActiveMain
import com.example.bookies_001.App
import com.example.bookies_001.R
import com.example.bookies_001.api.PurchaseAPI
import com.example.bookies_001.model.purchase.PerchaseProccessRequest
import com.example.bookies_001.model.purchase.UserpointRequest
import com.example.bookies_001.ui.purchase.action.DoPurchaseProcess
import com.example.bookies_001.ui.purchase.action.GetUserPoint
import com.example.bookies_001.ui.purchase.action.PurchaseProcessAdapter
import com.example.bookies_001.utils.SessionManager
import java.text.NumberFormat
import java.util.Locale

class PurchaseProcessFragment: Fragment() {

    private lateinit var purchaseProcessList: ListView
    private lateinit var purchaseProcessPoint: TextView
    private lateinit var purchaseProcessTotalPrice: TextView
    private lateinit var purchaseProcessStorePoint: TextView
    private lateinit var purchaseProcessButton: Button

    private var user_point: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (!SessionManager.isLoggedIn(requireContext())) {
            Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.listFragment) // 루트 페이지로 이동
            return view
        }

        val view = inflater.inflate(R.layout.purchase_process, container, false)
        val cart_list = arguments?.getSerializable("cart_list") as? ArrayList<MutableMap<String, Any>> ?: arrayListOf(mutableMapOf("title" to "Unknown", "price" to "0"))
        val total_price = arguments?.getInt("total_price") ?: 0
        val userId = SessionManager.getUserID(requireContext()).toString()

        purchaseProcessList = view.findViewById(R.id.purchase_process_list)
        purchaseProcessPoint = view.findViewById(R.id.purchase_process_point)
        purchaseProcessTotalPrice = view.findViewById(R.id.purchase_process_totalprice)
        purchaseProcessStorePoint = view.findViewById(R.id.purchase_process_storePoint)
        purchaseProcessButton = view.findViewById(R.id.purchase_process_button)

        purchaseProcessTotalPrice.setText("총 ${cart_list.size} 권, ${NumberFormat.getNumberInstance(Locale.US).format(total_price)} 원")

        val app = requireActivity().application as App
        val purchaseAPI = app.retrofit.create(PurchaseAPI::class.java)
        val adapter = PurchaseProcessAdapter(requireContext(), cart_list, purchaseAPI)
        purchaseProcessList.adapter = adapter

        val getUserPoint = GetUserPoint(purchaseAPI)
        val userPointData = UserpointRequest(
            user_id = userId
        )


        getUserPoint.getUserPoint(userPointData) { response, error ->
            if (response != null) {
                purchaseProcessPoint.text = NumberFormat.getNumberInstance(Locale.US).format(response.user_point)
                purchaseProcessStorePoint.text = "${NumberFormat.getNumberInstance(Locale.US).format(response.user_point - total_price)}"
                user_point = response.user_point
            } else {

            }
        }

        purchaseProcessButton.setOnClickListener {
            val doPurchaseProcess = DoPurchaseProcess(purchaseAPI)
            val purchaseProcessData = PerchaseProccessRequest(
                user_id = userId
            )

            if (user_point - total_price >= 0){

            doPurchaseProcess.doPurchaseProcess(purchaseProcessData) { response ->
                if (response != null) {
                    if (response.status){
                        // userPurchaseFragment로 이동 (purchaseFragment를 백 스택에서 제거)
                        findNavController().navigate(
                            R.id.action_purchaseProcessFragment_to_userPurchaseFragment,
                            null,
                            navOptions {
                                popUpTo(R.id.purchaseFragment) { inclusive = true }
                            }
                        )
                        // 네비게이션 메뉴의 상태를 userPurchaseFragment로 설정
                        (activity as? ActiveMain)?.apply {
                            navigationView.setCheckedItem(R.id.userPurchaseFragment)
                        }
                    } else if (user_point - total_price < 0) {
                        Toast.makeText(requireContext(), "포인트가 부족합니다.", Toast.LENGTH_SHORT).show()
                        // chargePointFragment로 이동 (purchaseFragment를 백 스택에서 제거)
                        findNavController().navigate(
                            R.id.action_purchaseProcessFragment_to_chargePointFragment,
                            null,
                            navOptions {
                                popUpTo(R.id.purchaseFragment) { inclusive = true }
                            }
                        )
                    } else {
                        Toast.makeText(requireContext(),"결제 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            } else {
                Toast.makeText(requireContext(), "포인트가 부족합니다.", Toast.LENGTH_SHORT).show()
                // chargePointFragment로 이동 (purchaseFragment를 백 스택에서 제거)
                findNavController().navigate(
                    R.id.action_purchaseProcessFragment_to_chargePointFragment,
                    null,
                    navOptions {
                        popUpTo(R.id.purchaseFragment) { inclusive = true }
                    }
                )
            }

        }

        return view
    }
}