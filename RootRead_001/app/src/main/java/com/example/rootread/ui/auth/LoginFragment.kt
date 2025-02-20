package com.example.rootread.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rootread.App
import com.example.rootread.R
import com.example.rootread.api.AuthAPI
import com.example.rootread.ui.auth.action.*
import com.example.rootread.utils.SessionManager

class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var inputID: EditText
    private lateinit var inputPW: EditText
    private lateinit var loginBT: Button
    private lateinit var findID: Button
    private lateinit var findPW: Button
    private lateinit var signupBT: Button
    private lateinit var remmemberID: CheckBox
    private lateinit var autoLogin: CheckBox

    private val LoginAction = LoginAction()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_main, container, false)


//        버튼 정의
        loginBT = view.findViewById(R.id.login_bt)
        findID = view.findViewById(R.id.find_id)
        findPW = view.findViewById(R.id.find_pw)
        signupBT = view.findViewById(R.id.signup)
        remmemberID = view.findViewById(R.id.remember_id)
        autoLogin = view.findViewById(R.id.auto_login)

        SessionManager.isRememberID(requireContext()).let {
            remmemberID.isChecked = it
        }
        SessionManager.isAutoLogin(requireContext()).let {
            autoLogin.isChecked = it
        }

        loginBT.setOnClickListener(this)
        findID.setOnClickListener(this)
        findPW.setOnClickListener(this)
        signupBT.setOnClickListener(this)

        inputID = view.findViewById(R.id.id_input)
        inputPW = view.findViewById(R.id.pw_input)

        if (SessionManager.isRememberID(requireContext())) {
            inputID.setText(SessionManager.getUserID(requireContext()))
        }

        return view

    }

    override fun onClick(v: View?) {
        val app = requireActivity().application as App
        val authAPI = app.retrofit.create(AuthAPI::class.java)
        val navController = findNavController()

        when (v?.id) {
            R.id.login_bt -> {
                LoginAction.doLogin(requireContext(), inputID, inputPW,remmemberID, autoLogin, authAPI, navController) { success ->
                    if (!success) {
                        Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            R.id.find_id -> {
                navController.navigate(R.id.action_loginFragment_to_findIdFragment)
            }
            R.id.find_pw -> {
                navController.navigate(R.id.action_loginFragment_to_findPWConfirmFragment)
            }
            R.id.signup -> {
                navController.navigate(R.id.action_loginFragment_to_signupFragment)
            }

        }
    }
}