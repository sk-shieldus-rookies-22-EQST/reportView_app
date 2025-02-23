package f0.c.rootread.ui.purchase

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
import f0.c.rootread.ActiveMain
import f0.c.rootread.App
import f0.c.rootread.R
import f0.c.rootread.api.PurchaseAPI
import f0.c.rootread.model.purchase.EachCartItem
import f0.c.rootread.model.purchase.PerchaseProccessRequest
import f0.c.rootread.model.purchase.UserpointRequest
import f0.c.rootread.ui.purchase.action.DoPurchaseProcess
import f0.c.rootread.ui.purchase.action.GetUserPoint
import f0.c.rootread.ui.purchase.action.PurchaseProcessAdapter
import f0.c.rootread.utils.SessionManager
import java.text.NumberFormat
import java.util.Locale

class PurchaseProcessFragment : Fragment() {

    private lateinit var purchaseProcessList: ListView
    private lateinit var purchaseProcessPoint: TextView
    private lateinit var purchaseProcessTotalPrice: TextView
    private lateinit var purchaseProcessStorePoint: TextView
    private lateinit var purchaseProcessButton: Button

    private var user_point: Int = 0
    private var total_price: Int = 0
    private var cart_list: ArrayList<EachCartItem> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ✅ 로그인이 필요한 경우 로그인 화면으로 이동 후 종료
        if (!SessionManager.isLoggedIn(requireContext())) {
            Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.listFragment) // 루트 페이지로 이동
            return null
        }

        val view = inflater.inflate(R.layout.purchase_process, container, false)

        // ✅ `Bundle`에서 데이터 받기 (`cart_list`를 올바른 타입으로 변환)
        cart_list = arguments?.getSerializable("cart_list") as? ArrayList<EachCartItem> ?: arrayListOf()
        total_price = arguments?.getInt("total_price") ?: 0
        val userId = SessionManager.getUserID(requireContext()).toString()

        // ✅ UI 요소 초기화
        purchaseProcessList = view.findViewById(R.id.purchase_process_list)
        purchaseProcessPoint = view.findViewById(R.id.purchase_process_point)
        purchaseProcessTotalPrice = view.findViewById(R.id.purchase_process_totalprice)
        purchaseProcessStorePoint = view.findViewById(R.id.purchase_process_storePoint)
        purchaseProcessButton = view.findViewById(R.id.purchase_process_button)

        // ✅ 총 가격 표시
        purchaseProcessTotalPrice.text = "총 ${cart_list.size} 권, ${NumberFormat.getNumberInstance(Locale.US).format(total_price)} 원"

        // ✅ 네트워크 통신 객체 생성
        val app = requireActivity().application as App
        val purchaseAPI = app.retrofit.create(PurchaseAPI::class.java)

        // ✅ 리스트 어댑터 설정
        val adapter = PurchaseProcessAdapter(requireContext(), cart_list, purchaseAPI)
        purchaseProcessList.adapter = adapter

        // ✅ 사용자 포인트 조회
        val getUserPoint = GetUserPoint(purchaseAPI)
        val userPointData = UserpointRequest(user_id = userId)

        getUserPoint.getUserPoint(userPointData) { response, error ->
            if (response != null) {
                user_point = response.user_point
                purchaseProcessPoint.text = NumberFormat.getNumberInstance(Locale.US).format(user_point)
                purchaseProcessStorePoint.text = NumberFormat.getNumberInstance(Locale.US).format(user_point - total_price)
            } else {
                Toast.makeText(requireContext(), "포인트 조회 실패", Toast.LENGTH_SHORT).show()
                user_point = 0 // 기본값 설정
            }
        }

        // ✅ 구매 버튼 클릭 리스너
        purchaseProcessButton.setOnClickListener {
            handlePurchaseProcess(purchaseAPI, userId)
        }

        return view
    }

    /**
     * ✅ 결제 처리 로직
     */
    private fun handlePurchaseProcess(purchaseAPI: PurchaseAPI, userId: String) {
        // ✅ 포인트 부족 예외 처리
        if (user_point < total_price) {
            Toast.makeText(requireContext(), "포인트가 부족합니다.", Toast.LENGTH_SHORT).show()
            navigateToChargePointFragment()
            return
        }

        // ✅ 결제 API 호출
        val doPurchaseProcess = DoPurchaseProcess(purchaseAPI)
        val purchaseProcessData = PerchaseProccessRequest(user_id = userId)

        doPurchaseProcess.doPurchaseProcess(purchaseProcessData) { response ->
            if (response?.status == true) {
                navigateToUserPurchaseFragment()
            } else {
                Toast.makeText(requireContext(), "결제 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * ✅ 유저 구매 내역 화면으로 이동
     */
    private fun navigateToUserPurchaseFragment() {
        findNavController().navigate(
            R.id.action_purchaseProcessFragment_to_userPurchaseFragment,
            null,
            navOptions {
                popUpTo(R.id.purchaseFragment) { inclusive = true }
            }
        )

        // ✅ 네비게이션 메뉴의 상태를 userPurchaseFragment로 설정
        (activity as? ActiveMain)?.apply {
            navigationView.setCheckedItem(R.id.userPurchaseFragment)
        }
    }

    /**
     * ✅ 포인트 충전 화면으로 이동
     */
    private fun navigateToChargePointFragment() {
        findNavController().navigate(
            R.id.action_purchaseProcessFragment_to_chargePointFragment,
            null,
            navOptions {
                popUpTo(R.id.purchaseFragment) { inclusive = true }
            }
        )
    }
}
