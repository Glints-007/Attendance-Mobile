package com.example.attendance.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.attendance.R
import com.example.attendance.utils.SharedPrefManager
import com.example.attendance.api.APIClient
import com.example.attendance.model.LoginResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var email_Login: TextInputEditText
    private lateinit var passLogin: TextInputEditText
    private lateinit var forgotPass: MaterialTextView
    private lateinit var loginBtn: MaterialButton
    private lateinit var registPage: MaterialTextView

    private lateinit var prefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email_Login = findViewById(R.id.email_login)
        passLogin = findViewById(R.id.pass_login)
        forgotPass = findViewById(R.id.forgot_pass_tv)
        loginBtn = findViewById(R.id.login_btn)
        registPage = findViewById(R.id.registTV)

        prefManager = SharedPrefManager(applicationContext)
        initAction()
    }

    private fun initAction(){
        forgotPass.setOnClickListener {
            val intent = Intent(this, ForgotPass::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            finish()
        }

        loginBtn.setOnClickListener {
            if (TextUtils.isEmpty(email_Login.text.toString())){
                email_Login.requestFocus()
                email_Login.error = "Please enter your email..."
            }
            if (TextUtils.isEmpty(passLogin.text.toString())){
                passLogin.requestFocus()
                passLogin.error = "Please enter your password..."
            }
            else{
                loginUser()
            }
        }

        registPage.setOnClickListener{
            val intent = Intent(this, Regist::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            finish()
        }
    }

    fun loginUser(){
        val email = email_Login.text.toString()
        val password = passLogin.text.toString()

        val loginRespCall: Call<LoginResponse> = APIClient.service.loginUser(email, password)
        loginRespCall.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val loginResponse = response.body()
                    if (loginResponse?.content?.status_code == 200){
                        prefManager.saveUser(loginResponse.content)
                        val intent = Intent(this@Login, MainActivity::class.java)
                        intent.putExtra("name", loginResponse.content.name)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                        finish()
                    }
                    else{
                        Toast.makeText(this@Login, loginResponse!!.msg, Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    val message = "Unable to login with the provided credential..."
                    Toast.makeText(this@Login, message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val message = t.localizedMessage
                Toast.makeText(this@Login, message, Toast.LENGTH_LONG).show()
            }
        })
    }
}