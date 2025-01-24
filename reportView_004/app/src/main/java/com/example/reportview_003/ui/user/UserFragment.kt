package com.example.reportview_003.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.UserAPI
import com.example.reportview_003.ui.user.action.GetUserinfo

class UserFragment : Fragment(), View.OnClickListener {

    private lateinit var userNameTextView : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.userinfo_main, container, false)

        userNameTextView = view.findViewById(R.id.userinfo_name)

        val app = requireActivity().application as App
        val userAPI = app.retrofit.create(UserAPI::class.java)

        val getUserinfo = GetUserinfo(requireContext(), userAPI)

        getUserinfo.loadUserinfo("admin") { userName ->
            if (isAdded) {
                userName?.let{
                    println("username: $userName")
                    userNameTextView.text = it // 사용자 이름을 TextView에 표시
                } ?: run {
                    Log.e("UserFragment", "Failed to load user info.")
                }
            } else {
                Log.e("ListFragment", "Fragment is not attached to a context while loading data.")
            }
        }

        return view
    }

    override fun onClick(v: View?) {
        true
    }
}
