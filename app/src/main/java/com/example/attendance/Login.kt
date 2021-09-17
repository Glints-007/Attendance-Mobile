package com.example.attendance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class Login : AppCompatActivity() {
    private lateinit var email_Login: TextInputEditText
    private lateinit var passLogin: TextInputEditText
    private lateinit var forgotPass: MaterialTextView
    private lateinit var loginBtn: MaterialButton
    private lateinit var registPage: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email_Login = findViewById(R.id.email_login)
        passLogin = findViewById(R.id.pass_login)
        forgotPass = findViewById(R.id.forgot_pass_tv)
        loginBtn = findViewById(R.id.login_btn)
        registPage = findViewById(R.id.registTV)

        initAction()
    }

    private fun initAction(){
//        forgotPass.setOnClickListener {
//            val intent = Intent(this, Regist::class.java)
//            startActivity(intent)
//            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
//            finish()
//        }

        loginBtn.setOnClickListener {
//            val loginReq = LoginReq()
            if (TextUtils.isEmpty(email_Login.text.toString())){
                email_Login.requestFocus()
                email_Login.setError("Please enter your email...")
            }
            if (TextUtils.isEmpty(passLogin.text.toString())){
                passLogin.requestFocus()
                passLogin.setError("Please enter your password...")
            }
            else{

            }
        }

        registPage.setOnClickListener{
            val intent = Intent(this, Regist::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            finish()
        }
    }
}