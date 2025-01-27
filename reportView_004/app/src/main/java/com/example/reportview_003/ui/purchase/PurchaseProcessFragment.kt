package com.example.reportview_003.ui.purchase

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
import com.example.reportview_003.ActiveMain
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.PurchaseAPI
import com.example.reportview_003.model.purchase.PerchaseProccessRequest
import com.example.reportview_003.ui.purchase.action.DoPurchaseProcess
import com.example.reportview_003.ui.purchase.action.PurchaseProcessAdapter
import com.example.reportview_003.utils.SessionManager

class PurchaseProcessFragment: Fragment() {

    private lateinit var purchaseProcessList: ListView
    private lateinit var purchaseProcessPoint: TextView
    private lateinit var purchaseProcessTotalPrice: TextView
    private lateinit var purchaseProcessStorePoint: TextView
    private lateinit var purchaseProcessButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (!SessionManager.isLoggedIn(requireContext())) {
            Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.listFragment) // 루트 페이지로 이동
            return null
        }

        val view = inflater.inflate(R.layout.purchase_process, container, false)
        val cart_id = arguments?.getInt("cart_id") ?: 0
        val cart_list = arguments?.getSerializable("cart_list") as? ArrayList<MutableMap<String, Any>> ?: arrayListOf(mutableMapOf("title" to "Unknown", "price" to "0"))
        val total_price = arguments?.getInt("total_price") ?: ""

        purchaseProcessList = view.findViewById(R.id.purchase_process_list)
        purchaseProcessPoint = view.findViewById(R.id.purchase_process_point)
        purchaseProcessTotalPrice = view.findViewById(R.id.purchase_process_totalprice)
        purchaseProcessStorePoint = view.findViewById(R.id.purchase_process_storePoint)
        purchaseProcessButton = view.findViewById(R.id.purchase_process_button)

        purchaseProcessTotalPrice.setText(total_price.toString())

        val app = requireActivity().application as App
        val purchaseAPI = app.retrofit.create(PurchaseAPI::class.java)
        val adapter = PurchaseProcessAdapter(requireContext(), cart_list, purchaseAPI)
        purchaseProcessList.adapter = adapter

        purchaseProcessButton.setOnClickListener {
            val doPurchaseProcess = DoPurchaseProcess(requireContext(), purchaseAPI)
            val purchaseProcessData = PerchaseProccessRequest(
                cart_id = cart_id,
                user_id = SessionManager.getUserID(requireContext()).toString()
            )
            doPurchaseProcess.doPurchaseProcess(purchaseProcessData) { response ->
                if (response != null) {
                    if (response.status){
                        // 이전 프래그먼트 제거 (purchaseFragment)
                        findNavController().popBackStack(R.id.purchaseFragment, true)

                        // userPurchaseFragment로 이동
                        findNavController().navigate(R.id.userPurchaseFragment)
                        // 네비게이션 메뉴의 상태를 userPurchaseFragment로 설정
                        (activity as? ActiveMain)?.apply {
                            navigationView.setCheckedItem(R.id.userPurchaseFragment)
                        }
                    } else{
                        Toast.makeText(requireContext(), "포인트가 부족합니다.", Toast.LENGTH_SHORT).show()
                        // 이전 프래그먼트 제거 (purchaseFragment)
                        findNavController().popBackStack(R.id.purchaseFragment, true)

                        // userPurchaseFragment로 이동
                        findNavController().navigate(R.id.chargePointFragment)
                        // 네비게이션 메뉴의 상태를 userPurchaseFragment로 설정
                        (activity as? ActiveMain)?.apply {
                            navigationView.setCheckedItem(0)
                        }
                    }
                }
            }
        }

        return view
    }
}