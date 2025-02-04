package com.example.bookies_001.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bookies_001.App
import com.example.bookies_001.R
import com.example.bookies_001.api.AuthAPI
import com.example.bookies_001.ui.auth.action.*
import com.example.bookies_001.utils.SessionManager

class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var inputID: EditText
    private lateinit var inputPW: EditText
    private lateinit var loginBT: Button
    private lateinit var findID: Button
    private lateinit var signupBT: Button

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
        signupBT = view.findViewById(R.id.signup)

        loginBT.setOnClickListener(this)
        findID.setOnClickListener(this)
        signupBT.setOnClickListener(this)

        inputID = view.findViewById(R.id.id_input)
        inputPW = view.findViewById(R.id.pw_input)

        return view

    }

    override fun onClick(v: View?) {
        val app = requireActivity().application as App
        val authAPI = app.retrofit.create(AuthAPI::class.java)
        val navController = findNavController()

        when (v?.id) {
            R.id.login_bt -> {
                LoginAction.doLogin(requireContext(), inputID, inputPW, authAPI, navController) { success ->
                    if (success) {
                        SessionManager.saveUserID(requireContext(), inputID.text.toString())
                    } else {
                        Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            R.id.find_id -> {
                navController.navigate(R.id.action_loginFragment_to_findIdFragment)
            }
            R.id.signup -> {
                navController.navigate(R.id.action_loginFragment_to_signupFragment)
            }

        }
    }
}