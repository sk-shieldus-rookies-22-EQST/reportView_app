package com.example.bookies_001.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bookies_001.App
import com.example.bookies_001.R
import com.example.bookies_001.api.AuthAPI
import com.example.bookies_001.api.KMSAPI
import com.example.bookies_001.model.auth.LoginRequest
import com.example.bookies_001.repository.AuthRepository
import com.example.bookies_001.repository.KmsRepository
import com.example.bookies_001.utils.AESUtil
import com.example.bookies_001.utils.DoRSAUtils
import com.example.bookies_001.utils.SessionManager

class UserConfirmFragment : Fragment() {

    private lateinit var inputPW : EditText
    private lateinit var confirmBtn : Button
    private lateinit var userID : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.user_info_confirm, container, false)

        inputPW = view.findViewById(R.id.input_passwd)
        confirmBtn = view.findViewById(R.id.confirm_button)
        userID = view.findViewById(R.id.user_info_user_id)

        userID.text = SessionManager.getUserID(requireContext())

        val app = requireActivity().application as App
        val authAPI = app.retrofit.create(AuthAPI::class.java)

        confirmBtn.setOnClickListener {
            val userId = SessionManager.getUserID(requireContext())
            val password = inputPW.text.toString()
            val plainText = "$userId&&&&$password"

            if (!DoRSAUtils.isInitialized()) {
                val kmsApi = app.KMSretrofit.create(KMSAPI::class.java)

                val kmsRepository = KmsRepository(kmsApi)
                DoRSAUtils.initialize(kmsRepository)
            }

            AESUtil.encrypt(plainText) { encryptedData ->
                if (encryptedData != null) {
                    val aesEncrypt = LoginRequest(e2e_data = encryptedData) // 🔹 암호화 완료 후 요청 생성

                    // 비밀번호 확인 로직 실행 (AES 암호화 후 요청 전송)
                    AuthRepository(authAPI).login(aesEncrypt) { response, error ->
                        if (response != null) {
                            if (response.status) {
                                findNavController().navigate(R.id.action_userConfirmFragment_to_userinfofragment)
                            } else {
                                Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                                inputPW.text = null
                            }
                        } else {
                            Toast.makeText(requireContext(), "서버 오류", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "AES 암호화 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }


        return view
    }
}