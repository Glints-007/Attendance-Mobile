package com.example.attendance.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.attendance.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

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
            var dialog = CustomDialogFragment()
            dialog.show(supportFragmentManager, "customDialog")

        }
    }
}