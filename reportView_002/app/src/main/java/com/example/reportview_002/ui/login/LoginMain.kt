package com.example.reportview_002.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.reportview_002.App
import com.example.reportview_002.R
import com.example.reportview_002.api.AuthAPI
import com.example.reportview_002.ui.login.action.*

class LoginMain : AppCompatActivity(), View.OnClickListener {

    lateinit var inputID: EditText
    lateinit var inputPW: EditText
    lateinit var loginBT: Button
    lateinit var findID: Button
    lateinit var findPW: Button

    private val LoginAction = LoginAction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)


//        버튼 정의
        loginBT = findViewById(R.id.login_bt)
        findID = findViewById(R.id.find_id)
        findPW = findViewById(R.id.find_pw)

        loginBT.setOnClickListener(this)
        findID.setOnClickListener(this)
        findPW.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        inputID = findViewById(R.id.id_input)
        inputPW = findViewById(R.id.pw_input)

        val app = application as App
        val authAPI = app.retrofit.create(AuthAPI::class.java)

        when (v?.id) {
            R.id.login_bt -> {
                LoginAction.doLogin(this, inputID, inputPW, authAPI)
            }
            R.id.find_id -> {

            }
            R.id.find_pw -> {

            }
        }
    }
}