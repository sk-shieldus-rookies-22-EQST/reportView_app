package com.example.reportview_003.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.AuthAPI
import com.example.reportview_003.model.auth.*
import com.example.reportview_003.ui.auth.action.FindPWAction

class FindPWFragment: Fragment(), View.OnClickListener {

    private lateinit var findPWID: EditText
    private lateinit var findPWPhone: EditText
    private lateinit var findPWEmail: EditText
    private lateinit var findPWButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.find_pw, container, false)

        findPWID = view.findViewById(R.id.find_pw_id)
        findPWPhone = view.findViewById(R.id.find_pw_phone)
        findPWEmail = view.findViewById(R.id.find_pw_email)
        findPWButton = view.findViewById(R.id.find_pw_button)

        findPWButton.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        val userID = findPWID.text.toString()
        val userPhone = findPWPhone.text.toString()
        val userEmail = findPWEmail.text.toString()

        val findPWRequest = FindPWRequest(
            user_id = userID,
            phone = userPhone,
            email = userEmail
        )

        val app = requireActivity().application as App
        val findPWApi = app.retrofit.create(AuthAPI::class.java)

        val findPWAction = FindPWAction(requireContext(), findPWApi)
        findPWAction.doFindPW(findPWRequest) { data ->
            if (isAdded) {
                if (data != null){
                    // pw 변경 로직 추가
                    Toast.makeText(requireContext(),"welcome",Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("PurchaseFragment", "Fragment is not attached to a context while loading data.")
                }
            }
        }
    }
}