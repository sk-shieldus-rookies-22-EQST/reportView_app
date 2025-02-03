package com.example.reportview_003.ui.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.reportview_003.ActiveMain
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.PurchaseAPI
import com.example.reportview_003.model.purchase.ChargePointRequest
import com.example.reportview_003.repository.PurchaseRepository
import com.example.reportview_003.utils.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PointFragment : Fragment() {

    private lateinit var pointView: EditText
    private lateinit var pointButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.charge_point, container, false)

        pointView = view.findViewById(R.id.point_view)
        pointButton = view.findViewById(R.id.point_button)

        val app = requireActivity().application as App
        val purchaseApi = app.retrofit.create(PurchaseAPI::class.java)

        pointButton.setOnClickListener {
            val chargeAmount = pointView.text.toString().toIntOrNull() ?: 0

            if (chargeAmount <= 0) {
                context?.let { ctx ->
                    Toast.makeText(ctx, "올바른 충전 금액을 입력하세요.", Toast.LENGTH_SHORT).show()
                }
                return@setOnClickListener
            }

            val chargePointRequest = ChargePointRequest(
                user_id = SessionManager.getUserID(requireContext()).toString(),
                charge_point = chargeAmount
            )

            val chargePointRepository = PurchaseRepository(purchaseApi)
            chargePointRepository.chargePoint(chargePointRequest) { response, error ->
                if (response != null) {
                    if (response.user_point > 0) {
                        context?.let { ctx ->
                            Toast.makeText(ctx, "${chargeAmount} 포인트가 충전되었습니다.", Toast.LENGTH_SHORT).show()
                        }

                        // 네비게이션 실행 전에 현재 Fragment 확인
                        viewLifecycleOwner.lifecycleScope.launch {
                            delay(100) // ✅ Fragment가 완전히 로드될 시간을 주기 위해 대기
                            if (isAdded && findNavController().currentDestination?.id == R.id.chargePointFragment) {
                                findNavController().navigate(R.id.action_chargePointFragment_to_purchaseFragment)
                            }
                        }
                    } else {
                        context?.let { ctx ->
                            Toast.makeText(ctx, "포인트 충전에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    error?.printStackTrace()
                }
            }
        }

        return view
    }
}
