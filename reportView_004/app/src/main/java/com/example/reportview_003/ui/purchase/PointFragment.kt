package com.example.reportview_003.ui.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reportview_003.ActiveMain
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.PurchaseAPI
import com.example.reportview_003.model.purchase.ChargePointRequest
import com.example.reportview_003.repository.PurchaseRepository
import com.example.reportview_003.utils.SessionManager

class PointFragment: Fragment() {

    private lateinit var pointView: EditText
    private lateinit var pointButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.charge_point, container, false)
        val navController = findNavController()

        pointView = view.findViewById(R.id.point_view)
        pointButton = view.findViewById(R.id.point_button)

        val app = requireContext().applicationContext as App
        val purchaseApi = app.retrofit.create(PurchaseAPI::class.java)

        val chargePointRequest = ChargePointRequest(
            user_id = SessionManager.getUserID(requireContext()).toString(),
            point = pointView.text.toString().toInt()
        )

        pointButton.setOnClickListener {
            val chargePointRepository = PurchaseRepository(purchaseApi)
            chargePointRepository.chargePoint(chargePointRequest) { response, error ->
                if (response != null) {
                    if (response.status){
                        // 이전 프래그먼트 제거 (purchaseFragment)
                        findNavController().popBackStack(R.id.chargePointFragment, true)

                        // userPurchaseFragment로 이동
                        findNavController().navigate(R.id.purchaseFragment)
                        // 네비게이션 메뉴의 상태를 PurchaseFragment로 설정
                        (activity as? ActiveMain)?.apply {
                            navController.navigate(R.id.action_chargePointFragment_to_purchaseFragment)
                        }

                        Toast.makeText(requireContext(), "${pointView.text.toString()} 포인트가 충전되었습니다. {}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "포인트 충전에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    error?.printStackTrace()
                }
            }
        }

        return view

    }
}