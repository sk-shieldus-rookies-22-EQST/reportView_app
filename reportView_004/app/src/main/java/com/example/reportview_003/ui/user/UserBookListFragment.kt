package com.example.reportview_003.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.UserAPI
import com.example.reportview_003.model.user.UserbooklistRequest
import com.example.reportview_003.ui.user.action.GetUserBookList
import com.example.reportview_003.ui.user.action.UserBookListAdapter
import com.example.reportview_003.utils.SessionManager

class UserBookListFragment: Fragment() {

    private lateinit var userBookList: RecyclerView
    private var bookData: MutableList<Map<String, Any>> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.user_book_main, container, false)

        val app = requireActivity().application as App
        val userAPI = app.retrofit.create(UserAPI::class.java)

        userBookList = view.findViewById(R.id.user_purchase_list)
        val userId = SessionManager.getUserID(requireContext()) ?: "default_user"

        val userbooklistRequest = UserbooklistRequest(user_id = userId)
        val getUserBookList = GetUserBookList(requireContext(), userAPI)

        getUserBookList.loadUserBooklist(userbooklistRequest) { response ->
            if (isAdded) {
                if (response != null) {
                    bookData = response.book_list
                    updateRecyclerView()
                }
            } else {
                Log.e("UserBookListFragment", "Fragment is not attached to a context.")
            }
        }

        return view
    }

    private fun updateRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2) // 2열로 설정
        userBookList.layoutManager = layoutManager
        val adapter = UserBookListAdapter(requireContext(), bookData)
        userBookList.adapter = adapter
    }

}