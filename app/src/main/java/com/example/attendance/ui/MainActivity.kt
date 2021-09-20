package com.example.attendance.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var logoutFab: ExtendedFloatingActionButton
    private lateinit var timeShow: TextView
    private lateinit var scheduleDate: TextView
    private lateinit var locatTV: TextView
    private lateinit var clockInBtn: MaterialButton
    private lateinit var clockOutBtn: MaterialButton
    private lateinit var timeLog: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logoutFab = findViewById(R.id.logout_fab)
        timeShow = findViewById(R.id.timeTV)
        scheduleDate = findViewById(R.id.scheduleTV)
        locatTV = findViewById(R.id.locationTV)
        clockInBtn = findViewById(R.id.clockIn_Btn)
        clockOutBtn = findViewById(R.id.clockOut_Btn)
        timeLog = findViewById(R.id.timeLogRV)

        timeShow.setText(getCurrentTime())
        scheduleDate.setText("${getDay()}, ${getTodayState()}")
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
}