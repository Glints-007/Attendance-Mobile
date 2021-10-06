package com.example.attendance.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.attendance.R
import com.example.attendance.api.APIClient
import com.example.attendance.model.ForgotResponse
import com.example.attendance.utils.ErrorUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPass : AppCompatActivity()  {
    private lateinit var email: TextInputEditText
    private lateinit var sendBtn: MaterialButton
    private lateinit var back: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        email = findViewById(R.id.email_forgot)
        sendBtn = findViewById(R.id.send_btn)
        back = findViewById(R.id.login_page_tv)

        sendBtn.setOnClickListener {
            forgotPass()
        }

        back.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
            finish()
        }
    }

    fun  forgotPass(){
        val email = email.text.toString()
        val forgotRespCall: Call<ForgotResponse> =APIClient.service.forgotPass(email)
        forgotRespCall.enqueue(object : Callback<ForgotResponse>{
            override fun onResponse(call: Call<ForgotResponse>, response: Response<ForgotResponse>) {
                if (response.isSuccessful){
                    Toast.makeText(this@ForgotPass, response.body()!!.data!!.message, Toast.LENGTH_LONG).show()

                    val dialog = CustomDialogFragment()
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
                    dialog.show(supportFragmentManager, "customDialog")
                }
                else{
                    val apiError = ErrorUtils.parseError(response)
                    Toast.makeText(this@ForgotPass, apiError.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ForgotResponse>, t: Throwable) {
                val message = t.localizedMessage
                Toast.makeText(this@ForgotPass, message, Toast.LENGTH_LONG).show()
            }
        })
    }
}