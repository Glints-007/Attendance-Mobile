package com.example.attendance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class Login : AppCompatActivity() {
    private lateinit var emailLogin: TextInputEditText
    private lateinit var passLogin: TextInputEditText
    private lateinit var forgotPass: MaterialTextView
    private lateinit var loginBtn: MaterialButton
    private lateinit var registPage: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailLogin = findViewById(R.id.email_login)
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

        registPage.setOnClickListener{
            val intent = Intent(this, Regist::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            finish()
        }
    }
}