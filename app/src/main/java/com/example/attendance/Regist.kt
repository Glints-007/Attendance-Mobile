package com.example.attendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class Regist : AppCompatActivity() {
    private lateinit var fullname: TextInputEditText
    private lateinit var emailRegist: TextInputEditText
    private lateinit var passRegist: TextInputEditText
    private lateinit var confirmPass: TextInputEditText
    private lateinit var registBtn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist)

        fullname = findViewById(R.id.name_regist)
        emailRegist = findViewById(R.id.email_regist)
        passRegist = findViewById(R.id.pass_regist)
        confirmPass = findViewById(R.id.confirm_pass)
        registBtn = findViewById(R.id.regist_btn)

//        initAction()
    }

    private fun initAction() {

    }
}