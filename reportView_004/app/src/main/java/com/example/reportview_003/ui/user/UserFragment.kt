package com.example.reportview_003.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.UserAPI
import com.example.reportview_003.model.user.UserupdateRequest
import com.example.reportview_003.ui.user.action.GetUserinfo
import com.example.reportview_003.utils.SessionManager

class UserFragment : Fragment() {

    private lateinit var userNameTextView : TextView
    private lateinit var userPwTextView : TextView
    private lateinit var userPwChTextView : TextView
    private lateinit var userPhoneTextView : TextView
    private lateinit var userEmailTextView : TextView
    private lateinit var userInfoBtn : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.userinfo_main, container, false)

        userNameTextView = view.findViewById(R.id.userinfo_name)
        userPwTextView = view.findViewById(R.id.userinfo_pw)
        userPwChTextView = view.findViewById(R.id.userinfo_pw_ch)
        userPhoneTextView = view.findViewById(R.id.userinfo_phone)
        userEmailTextView = view.findViewById(R.id.userinfo_email)
        userInfoBtn = view.findViewById(R.id.userinfo_button)

        val app = requireActivity().application as App
        val userAPI = app.retrofit.create(UserAPI::class.java)

        val getUserinfo = GetUserinfo(requireContext(), userAPI)

        val userID = SessionManager.getUserID(requireContext()).toString()

        getUserinfo.loadUserinfo(userID) { userName ->
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

        userInfoBtn.setOnClickListener {
            val userPw = userPwTextView.text.toString()
            val userPwCh = userPwChTextView.text.toString()
            val userPhone = userPhoneTextView.text.toString()
            val userEmail = userEmailTextView.text.toString()

            if (userPw != userPwCh) {
                Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userPw.isEmpty() || userPhone.isEmpty() || userEmail.isEmpty()) {
                Toast.makeText(requireContext(), "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userUpdateRequest = UserupdateRequest(
                user_id = userID,
                user_pw = userPw,
                user_phone = userPhone,
                user_email = userEmail
            )


            getUserinfo.updateUserInfo(userUpdateRequest){ success ->
                if (success) {
                    Toast.makeText(requireContext(), "정보 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "정보 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }


        return view
    }

}
