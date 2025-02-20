package com.example.rootread.ui.auth

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
import com.example.rootread.App
import com.example.rootread.R
import com.example.rootread.api.AuthAPI
import com.example.rootread.model.auth.FindIDRequest
import com.example.rootread.ui.auth.action.FindIDAction

class FindPWFragment : Fragment() {

    private lateinit var userID : EditText
    private lateinit var userPhone : EditText
    private lateinit var userMail : EditText
    private lateinit var confirmBtn : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.find_pw_confirm, container, false)

        userID = view.findViewById(R.id.user_id)
        userPhone = view.findViewById(R.id.user_phone)
        userMail = view.findViewById(R.id.user_mail)
        confirmBtn = view.findViewById(R.id.confirm_find_pw_button)

        val app = requireActivity().application as App
        val findIDapi = app.retrofit.create(AuthAPI::class.java)

        confirmBtn.setOnClickListener {
            val findIDRequest = FindIDRequest(
                user_phone = userPhone.text.toString(),
                user_email = userMail.text.toString()
            )

            val findIDaction = FindIDAction(requireContext(),findIDapi)
            findIDaction.doFindid(findIDRequest) { data ->
                if (isAdded) {
                    if (data != null) {
                        if (data != userID.text.toString()) {
                            Toast.makeText(requireContext(), "회원정보와 ID가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            val navController = findNavController()
                            val bundle = Bundle().apply {
                                putString("user_id", data)
                            }
                            navController.navigate(R.id.action_findPWConfirmFragment_to_findPWFragment, bundle)
                        }
                    } else {
                        Log.e("FindIDFragment", "Fragment is not attached to a context while loading data.")
                    }
                }
            }

        }

        return view
    }
}