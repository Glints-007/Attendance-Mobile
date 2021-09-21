package com.example.attendance.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.attendance.R
import com.example.attendance.api.APIClient
import com.example.attendance.model.ResetPassResp
import kotlinx.android.synthetic.main.fragment_dialog.*
import kotlinx.android.synthetic.main.fragment_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomDialogFragment: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_dialog, container, false)

        rootView.reset_btn.setOnClickListener {
            if (TextUtils.isEmpty(email_reset.text.toString())){
                email_reset.requestFocus()
                email_reset.setError("Please enter your email...")
            }
            if (TextUtils.isEmpty(token_reset.text.toString())){
                token_reset.requestFocus()
                token_reset.setError("Please enter the token that has been sent to your email...")
            }
            if (TextUtils.isEmpty(pass_reset.text.toString())){
                pass_reset.requestFocus()
                pass_reset.setError("Please enter your new password...")
            }
            if (confirm_pass_reset.text.toString() != pass_reset.text.toString()){
                confirm_pass_reset.requestFocus()
                confirm_pass_reset.setError("Wrong password...")
            }
            else {
                resetPass()
            }
        }
        return rootView
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(R.drawable.border)
//        window.setGravity(Gravity.BOTTOM)

        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun resetPass(){
        val email = email_reset.text.toString()
        val token = token_reset.text.toString()
        val password = pass_reset.text.toString()
        val confirm_password = confirm_pass_reset.text.toString()

        val resetRespCall: Call<ResetPassResp> = APIClient.service.resetPass(email, token, password, confirm_password)
        resetRespCall.enqueue(object : Callback<ResetPassResp>{
            override fun onResponse(call: Call<ResetPassResp>, response: Response<ResetPassResp>) {
                if (response.isSuccessful){
                    Toast.makeText(context, response.body()!!.msg, Toast.LENGTH_LONG).show()
                    dismiss()
                }
                else{
                    val message = "An error occurred\n Please try again later..."
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResetPassResp>, t: Throwable) {
                val message = t.localizedMessage
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        })
    }
}