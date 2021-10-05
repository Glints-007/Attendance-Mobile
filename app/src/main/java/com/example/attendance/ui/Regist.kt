package com.example.attendance.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.attendance.R
import com.example.attendance.api.APIClient
import com.example.attendance.model.RegistResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            if (TextUtils.isEmpty(emailRegist.text.toString()) or !Patterns.EMAIL_ADDRESS.matcher(emailRegist.text.toString()).matches()){
                emailRegist.requestFocus()
                emailRegist.error = "Please enter your email correctly..."
            }
            if (TextUtils.isEmpty(passRegist.text.toString())){
                passRegist.requestFocus()
                passRegist.error = "Please enter your password..."
            }
            if (passRegist.text.toString().length < 8){
                passRegist.requestFocus()
                passRegist.error = "Password contained 8 characters or more.."
            }
            if (TextUtils.isEmpty(confirmPass.text.toString())){
                confirmPass.requestFocus()
                confirmPass.error = "Please confirm your password..."
            }
            if (confirmPass.text.toString() != passRegist.text.toString()){
                confirmPass.requestFocus()
                confirmPass.error = "Wrong password..."
            }
            else{
                registUser()
            }
        }

        loginPage.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
            finish()
        }
    }

    fun registUser(){
        val name = fullname.text.toString()
        val email = emailRegist.text.toString()
        val password = passRegist.text.toString()

        val registRespCall: Call<RegistResponse> =APIClient.service.registUser(name, email, password)
        registRespCall.enqueue(object : Callback<RegistResponse>{
            override fun onResponse(call: Call<RegistResponse>, response: Response<RegistResponse>
            ) {if (response.isSuccessful){
                Toast.makeText(this@Regist, response.body()!!.msg, Toast.LENGTH_LONG).show()

                val intent = Intent(this@Regist, Login::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
                finish()
            }
            else{
                val message = "An error occurred\n Please try again later..."
                Toast.makeText(this@Regist, message, Toast.LENGTH_LONG).show()
            }
            }

            override fun onFailure(call: Call<RegistResponse>, t: Throwable) {
                val message = t.localizedMessage
                Toast.makeText(this@Regist, message, Toast.LENGTH_LONG).show()
            }
        })
    }
}