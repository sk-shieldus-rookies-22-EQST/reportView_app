package com.example.bookies_001.ui.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bookies_001.App
import com.example.bookies_001.R
import com.example.bookies_001.api.PurchaseAPI
import com.example.bookies_001.model.purchase.ChargePointRequest
import com.example.bookies_001.model.purchase.UserpointRequest
import com.example.bookies_001.repository.PurchaseRepository
import com.example.bookies_001.utils.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class PointFragment : Fragment() {

    private lateinit var userPoint : TextView
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
        userPoint = view.findViewById(R.id.user_point)

        val app = requireActivity().application as App
        val purchaseApi = app.retrofit.create(PurchaseAPI::class.java)
        val chargePointRepository = PurchaseRepository(purchaseApi)

        val userId = SessionManager.getUserID(requireContext()).toString()

        val userpointRequest = UserpointRequest(
            user_id = userId
        )

        chargePointRepository.userPoint(userpointRequest) { response, error ->
            if (response != null) {
                val userPointValue = response.user_point?.toString()?.toLongOrNull() ?: 0L
                val formattedUserPoint = NumberFormat.getNumberInstance(Locale.US).format(userPointValue)
                userPoint.setText(formattedUserPoint)
            } else {
                userPoint.setText("0") // 기본값 설정
            }
        }

        pointButton.setOnClickListener {
            val chargeAmount = pointView.text.toString().toIntOrNull() ?: 0

            if (chargeAmount <= 0) {
                context?.let { ctx ->
                    Toast.makeText(ctx, "올바른 충전 금액을 입력하세요.", Toast.LENGTH_SHORT).show()
                }
                return@setOnClickListener
            }

            val chargePointRequest = ChargePointRequest(
                user_id = userId,
                charge_point = chargeAmount
            )

            chargePointRepository.chargePoint(chargePointRequest) { response, error ->
                if (response != null) {
                    if (response.user_point > 0) {
                        context?.let { ctx ->
                            Toast.makeText(ctx, "${chargeAmount} 포인트가 충전되었습니다.", Toast.LENGTH_SHORT).show()
                        }

                        // 네비게이션 실행 전에 현재 Fragment 확인
                        findNavController().navigate(R.id.action_chargePointFragment_to_purchaseFragment)
                        }
                    } else {
                        context?.let { ctx ->
                            Toast.makeText(ctx, "포인트 충전에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        return view
    }
}
