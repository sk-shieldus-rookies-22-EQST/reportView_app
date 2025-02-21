package com.example.rootread.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rootread.App
import com.example.rootread.R
import com.example.rootread.api.UserAPI
import com.example.rootread.model.user.UserEachBook
import com.example.rootread.model.user.UserbooklistRequest
import com.example.rootread.ui.user.action.GetUserBookList
import com.example.rootread.ui.user.action.UserBookListAdapter
import com.example.rootread.utils.SessionManager

class UserBookListFragment: Fragment() {

    private lateinit var userBookList: RecyclerView
    private var bookData: MutableList<UserEachBook> = mutableListOf()

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
            if (!isAdded) {
                Log.e("UserBookListFragment", "Fragment is not attached to a context.")
                return@loadUserBooklist
            }

            if (response?.myBookListDtoList.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "구매한 책이 없습니다.", Toast.LENGTH_SHORT).show()
                return@loadUserBooklist
            }

            bookData.clear()  // ✅ 기존 데이터 초기화
            if (response != null) {
                bookData.addAll(response.myBookListDtoList)
            } // ✅ 새로운 데이터 추가
            updateRecyclerView()
        }

        return view
    }

    private fun updateRecyclerView() {
        if (!isAdded) return // ✅ Fragment가 살아있는지 확인 후 실행

        val layoutManager = GridLayoutManager(requireContext(), 2) // 2열 설정
        userBookList.layoutManager = layoutManager
        val adapter = UserBookListAdapter(requireContext(), bookData)
        userBookList.adapter = adapter
    }

}