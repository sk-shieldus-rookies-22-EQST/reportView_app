package com.example.bookies_001.ui.auth

import android.os.Bundle
import android.util.Log
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
import com.example.bookies_001.api.Api
import com.example.bookies_001.model.api.SignupRequest
import com.example.bookies_001.ui.auth.action.SignupAction

class SignupFragment: Fragment(), View.OnClickListener {

    private lateinit var signupID: EditText
    private lateinit var signupPW: EditText
    private lateinit var signupPWCheck: EditText
    private lateinit var signupPhone: EditText
    private lateinit var signupEmail: EditText
    private lateinit var signupButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.signup_main, container, false)

        signupID = view.findViewById(R.id.signup_id)
        signupPW = view.findViewById(R.id.signup_pw)
        signupPWCheck = view.findViewById(R.id.signup_pw_check)
        signupPhone = view.findViewById(R.id.signup_phone)
        signupEmail = view.findViewById(R.id.signup_email)
        signupButton = view.findViewById(R.id.signup_button)

        signupButton.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {

        val userID = signupID.text.toString()
        val userPW = signupPW.text.toString()
        val userPWCheck = signupPWCheck.text.toString()
        val userPhone = signupPhone.text.toString()
        val userEmail = signupEmail.text.toString()

        if (userPW != userPWCheck) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        val signupRequest = SignupRequest(
            user_id = userID,
            user_pw = userPW,
            user_phone = userPhone,
            user_email = userEmail
        )

        val app = requireActivity().application as App
        val signupApi = app.retrofit.create(Api::class.java)

        val signupAction = SignupAction(requireContext(), signupApi)
        signupAction.doSignup(signupRequest) { data ->
            if (isAdded) {
                if (data != null){
                    if (data) {
                        Toast.makeText(requireContext(), "회원가입에 성공하셨습니다! 환영합니다", Toast.LENGTH_SHORT).show()
                        val navController = findNavController()
                        navController.navigate(R.id.action_signupFragment_to_loginFragment)
                    } else {
                        Toast.makeText(requireContext(), "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("PurchaseFragment", "Fragment is not attached to a context while loading data.")
                }
            }
        }
    }

}