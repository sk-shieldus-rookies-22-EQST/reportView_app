package com.example.bookies_001.ui.user

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bookies_001.ActiveMain
import com.example.bookies_001.App
import com.example.bookies_001.R
import com.example.bookies_001.api.UserAPI
import com.example.bookies_001.model.user.SignoutRequest
import com.example.bookies_001.model.user.UserupdateRequest
import com.example.bookies_001.ui.user.action.GetUserinfo
import com.example.bookies_001.utils.SessionManager

class UserFragment : Fragment() {

    private lateinit var userNameTextView : TextView
    private lateinit var userPwTextView : TextView
    private lateinit var userPwChTextView : TextView
    private lateinit var userPhoneTextView : TextView
    private lateinit var userEmailTextView : TextView
    private lateinit var userInfoBtn : Button
    private lateinit var signout : TextView

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
        signout = view.findViewById(R.id.signout)

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

        signout.setOnClickListener {
            // 모달 창을 띄워서 사용자 비밀번호를 입력 받고 서버에 전송
            // 응답 값에 따라 [ true / false ] 동작을 수행
            // true: 탈퇴 완료 및 세션 삭제
            // false: 탈퇴 실패 fragment에 그대로 남아있음
            val input = EditText(requireContext()).apply {
                hint = "비밀번호"
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                setPadding(50, 30, 50,30)

            }

//            // EditText를 감싸는 레이아웃 (Margin 적용)
//            val container = FrameLayout(requireContext()).apply {
//                val params = FrameLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//                )
//                params.setMargins(150, 20, 150, 20) // ✅ Margin 적용 (좌우 50, 위아래 20)
//                layoutParams = params
//                addView(input)
//            }

            AlertDialog.Builder(requireContext())
                .setTitle("회원 탈퇴")
                .setMessage("${userID} 의 비밀번호를 입력해 주세요")
                .setView(input)
                .setPositiveButton("확인") { _, _ ->
                    val password = input.text.toString()
                    if (password.isNotEmpty()) {
                        val signoutRequest = SignoutRequest(
                            user_id = userID,
                            user_pw = password)  // ✅ 입력한 비밀번호 전달
                        getUserinfo.signoutUser(signoutRequest) { success ->
                            if (success.status) {
                                SessionManager.clearSession(requireContext())
                                (requireContext() as ActiveMain).updateNavigationMenu()
                                findNavController().navigate(R.id.action_userinfofragment_to_listFragment)
                            } else {
                                Toast.makeText(context, "비밀번호를 다시 확인 해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("취소", null)
                .show()
        }

        userInfoBtn.setOnClickListener {
            val userPw = userPwTextView.text.toString().ifEmpty { null }
            val userPwCh = userPwChTextView.text.toString().ifEmpty { null }
            val userPhone = userPhoneTextView.text.toString().ifEmpty { null }
            val userEmail = userEmailTextView.text.toString().ifEmpty { null }

            if (userPw != userPwCh) {
                Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
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
