package com.example.attendance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.example.attendance.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class Regist : AppCompatActivity() {
    private lateinit var fullname: TextInputEditText
    private lateinit var emailRegist: TextInputEditText
    private lateinit var passRegist: TextInputEditText
    private lateinit var confirmPass: TextInputEditText
    private lateinit var registBtn: MaterialButton
    private lateinit var loginPage: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist)

        fullname = findViewById(R.id.name_regist)
        emailRegist = findViewById(R.id.email_regist)
        passRegist = findViewById(R.id.pass_regist)
        confirmPass = findViewById(R.id.confirm_pass)
        registBtn = findViewById(R.id.regist_btn)
        loginPage = findViewById(R.id.loginTV)

        initAction()
    }

    private fun initAction() {
        registBtn.setOnClickListener {
            if (TextUtils.isEmpty(emailRegist.text.toString())){
                emailRegist.requestFocus()
                emailRegist.setError("Please enter your email...")
            }
            if (TextUtils.isEmpty(passRegist.text.toString())){
                passRegist.requestFocus()
                passRegist.setError("Please enter your password...")
            }
            if (TextUtils.isEmpty(confirmPass.text.toString())){
                confirmPass.requestFocus()
                confirmPass.setError("Please confirm your password...")
            }
            if (confirmPass.text.toString() != passRegist.text.toString()){
                confirmPass.requestFocus()
                confirmPass.setError("Wrong password...")
            }

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
            finish()
        }

        loginPage.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
            finish()
        }
    }
}