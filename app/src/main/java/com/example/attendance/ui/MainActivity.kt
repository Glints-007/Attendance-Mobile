package com.example.attendance.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance.R
import com.example.attendance.SharedPrefManager
import com.example.attendance.api.APIClient
import com.example.attendance.model.LogoutResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var logoutFab: ExtendedFloatingActionButton
    private lateinit var username: TextView
    private lateinit var timeShow: TextView
    private lateinit var scheduleDate: TextView
    private lateinit var locatTV: TextView
    private lateinit var clockInBtn: MaterialButton
    private lateinit var clockOutBtn: MaterialButton
    private lateinit var timeLog: RecyclerView

    private lateinit var prefManager: SharedPrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefManager = SharedPrefManager(applicationContext)

        logoutFab = findViewById(R.id.logout_fab)
        username = findViewById(R.id.name_user)
        timeShow = findViewById(R.id.timeTV)
        scheduleDate = findViewById(R.id.scheduleTV)
        locatTV = findViewById(R.id.locationTV)
        clockInBtn = findViewById(R.id.clockIn_Btn)
        clockOutBtn = findViewById(R.id.clockOut_Btn)
        timeLog = findViewById(R.id.timeLogRV)

        timeShow.setText(getCurrentTime())
        scheduleDate.setText("${getDay()}, ${getTodayState()}")

        val name = intent.getStringExtra("name")
        username.setText(name)
        
        init()
    }

    private fun init(){
        logoutFab.setOnClickListener {
            logout()
        }
    }
    fun getCurrentTime(): String {
        return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
    }

    fun getDay(): String {
        return SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
    }
    fun getTodayState(): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }

    fun logout(){
        val logoutRespCall: Call<LogoutResponse> = APIClient.service.logoutUser("Bearer ${prefManager.token}")
        logoutRespCall.enqueue(object : Callback<LogoutResponse>{
            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>
            ) {
                if (response.isSuccessful){
                    prefManager.clear()

                    Toast.makeText(this@MainActivity, "You  have been logged out...", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@MainActivity, Login::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
                    finish()
                }
                else{
                    val message = "Something went wrong\nPlease try again later..."
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                val message = t.localizedMessage
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
            }
        })
    }
}