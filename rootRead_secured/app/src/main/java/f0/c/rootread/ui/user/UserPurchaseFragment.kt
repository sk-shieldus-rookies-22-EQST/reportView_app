package f0.c.rootread.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import f0.c.rootread.App
import f0.c.rootread.R
import f0.c.rootread.api.UserAPI
import f0.c.rootread.model.user.EachPurchase
import f0.c.rootread.ui.user.action.GetUserPurchase
import f0.c.rootread.ui.user.action.BuildPurchaseList
import f0.c.rootread.utils.SessionManager

class UserPurchaseFragment: Fragment() {

    private lateinit var userpurchaseList: ListView
    private var purchaseData: MutableList<EachPurchase> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.user_purchase_main, container, false)

        val app = requireActivity().application as App
        val userAPI = app.retrofit.create(UserAPI::class.java)

        userpurchaseList = view.findViewById(R.id.user_purchase_list)

        var userId = SessionManager.getUserID(requireContext()) ?: "not logined"
        val userpurchaseRequest =
            f0.c.rootread.model.user.UserpurchaseRequest(user_id = userId)

        val getUserPurchase = GetUserPurchase(requireContext(), userAPI)
        getUserPurchase.loadUserPurchase(userpurchaseRequest) { response ->
            if (isAdded) {
                if (response != null) {
                    purchaseData = response.myPurchaseDto
                    if (purchaseData != null) {
                        updateListView()
                    }
                }
            } else {
                Log.e("UserPurchaseFragment", "Fragment is not attached to a context while loading data.")
            }

        }

        return view
    }

    private fun updateListView() {
        val adapter = BuildPurchaseList(requireContext(), purchaseData, findNavController())
        userpurchaseList.adapter = adapter
    }
}