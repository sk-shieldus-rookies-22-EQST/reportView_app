package com.example.reportview_003.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.AuthAPI
import com.example.reportview_003.model.auth.FindIDRequest
import com.example.reportview_003.ui.auth.action.FindIDAction

class FindIDFragment : Fragment(), View.OnClickListener {

    private lateinit var findIDphone: EditText
    private lateinit var findIDemail: EditText
    private lateinit var findIDbutton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.find_id, container, false)

        findIDphone = view.findViewById(R.id.find_id_phone)
        findIDemail = view.findViewById(R.id.find_id_email)
        findIDbutton = view.findViewById(R.id.find_id_button)

        findIDbutton.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {

        val userPhone = findIDphone.text.toString()
        val userEmail = findIDemail.text.toString()

        val findIDrequest = FindIDRequest(
            phone = userPhone,
            email = userEmail
        )

        val app = requireActivity().application as App
        val findIDapi = app.retrofit.create(AuthAPI::class.java)

        val findIDaction = FindIDAction(requireContext(),findIDapi)
        findIDaction.doFindid(findIDrequest) { data ->
            if (isAdded) {
                if (data != null) {
                    println(data)
                } else {
                    Log.e("FindIDFragment", "Fragment is not attached to a context while loading data.")
                }
            }
        }

    }
}