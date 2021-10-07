package com.example.attendance.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance.R
import com.example.attendance.utils.SharedPrefManager
import com.example.attendance.api.APIClient
import com.example.attendance.model.ClockHistoryResponse
import com.example.attendance.model.ClockInResponse
import com.example.attendance.model.ClockOutResponse
import com.example.attendance.model.LogoutResponse
import com.example.attendance.utils.Adapter
import com.example.attendance.utils.ErrorUtils
import com.google.android.gms.location.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var logoutFab: ExtendedFloatingActionButton
    private lateinit var username: TextView
    private lateinit var timeShow: TextView
    private lateinit var scheduleDate: TextView
    private lateinit var locatTV: TextView
    private lateinit var clockInBtn: MaterialButton
    private lateinit var clockOutBtn: MaterialButton
    private lateinit var refreshTV: TextView
    private lateinit var timeLog: RecyclerView

    private lateinit var prefManager: SharedPrefManager

    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private var lat by Delegates.notNull<Double>()
    private var long by Delegates.notNull<Double>()

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }

    private val locationRequest: LocationRequest =  LocationRequest.create().apply {
        interval = TimeUnit.SECONDS.toMillis(60)
        fastestInterval = TimeUnit.SECONDS.toMillis(30)
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime= TimeUnit.MINUTES.toMillis(2)
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                //The last location in the list is the newest
                val location = locationList.last()

                locatTV.text = "Your location: ${getCityName(location.latitude, location.longitude)}"
                locatTV.visibility = View.VISIBLE
            }
        }
    }

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
        refreshTV = findViewById(R.id.refreshTV)
        timeLog = findViewById(R.id.timeLogRV)

        timeShow.text = getCurrentTime()
        scheduleDate.text = getTodayState()

        val name = intent.getStringExtra("name")
        username.text = name

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        // Recycler View
        timeLog.setHasFixedSize(true)
        timeLog.layoutManager = LinearLayoutManager(this)
        getLog()

        initAction()
    }

    private fun initAction(){
        logoutFab.setOnClickListener {
            logout()
        }

        clockInBtn.setOnClickListener {
            clockIn()
        }

        clockOutBtn.setOnClickListener {
            clockOut()
        }

        refreshTV.setOnClickListener {
            getLog()
        }
    }

    override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
        else {
            getLastLocation()
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProvider.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProvider.removeLocationUpdates(locationCallback)
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION)
            return
        }
        fusedLocationProvider.lastLocation.addOnSuccessListener(this) { location ->
            lat = location.latitude
            long = location.longitude

            locatTV.text = "Your location: ${getCityName(lat, long)}"
            locatTV.visibility = View.VISIBLE
        }
    }

    private fun checkPermissions(): Boolean {
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        return false
    }

    private fun getCityName(lat: Double,long: Double):String{
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat,long,3)

        val cityName = address[0].locality
        val countryName = address[0].countryName
        return "$cityName, $countryName"
    }

    private fun startLocationPermissionRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    private fun requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ||
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            AlertDialog.Builder(this)
                .setTitle("Location Permission Required")
                .setMessage("This app needs the Location permission, please accept to use location functionality")
                .setPositiveButton("OK") { _, _ ->
                    //Prompt the user once explanation has been shown
                    startLocationPermissionRequest()
                }
                .create()
                .show()
        }
        else {
            // No explanation needed, we can request the permission straight away.
            startLocationPermissionRequest()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            when (requestCode) {
                MY_PERMISSIONS_REQUEST_LOCATION -> {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        ) {
                            fusedLocationProvider.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                Looper.getMainLooper()
                            )
                        }
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                    }
                    return
                }
            }
        }
    }

    fun getCurrentTime(): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    }
    fun getTodayState(): String {
        return SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault()).format(Date())
    }

    private fun logout(){
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
                    val apiError = ErrorUtils.parseError(response)
                    Toast.makeText(this@MainActivity, apiError.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                val message = t.localizedMessage
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun clockIn(){
        val clockInRespCall: Call<ClockInResponse> = APIClient.service.clockIn(
            "Bearer ${prefManager.token}", lat, long)
        clockInRespCall.enqueue(object: Callback<ClockInResponse>{
            override fun onResponse(call: Call<ClockInResponse>, response: Response<ClockInResponse>) {
                if (response.isSuccessful){
                        Toast.makeText(this@MainActivity, response.body()!!.msg, Toast.LENGTH_LONG).show()
                }
                else{
                    val apiError = ErrorUtils.parseError(response)
                    Toast.makeText(this@MainActivity, apiError.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ClockInResponse>, t: Throwable) {
                val message = t.localizedMessage
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun clockOut(){
        val clockOutRespCall: Call<ClockOutResponse> = APIClient.service.clockOut(
            "Bearer ${prefManager.token}", lat, long)
        clockOutRespCall.enqueue(object: Callback<ClockOutResponse>{
            override fun onResponse(call: Call<ClockOutResponse>, response: Response<ClockOutResponse>) {
                if (response.isSuccessful){
                    Toast.makeText(this@MainActivity, response.body()!!.msg, Toast.LENGTH_LONG).show()
                }
                else{
                    val apiError = ErrorUtils.parseError(response)
                    Toast.makeText(this@MainActivity, apiError.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ClockOutResponse>, t: Throwable) {
                val message = t.localizedMessage
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getLog() {
        APIClient.service.clockHistory("Bearer ${prefManager.token}")
            .enqueue(object : Callback<List<ClockHistoryResponse>>{
                override fun onResponse(call: Call<List<ClockHistoryResponse>>,
                                        response: Response<List<ClockHistoryResponse>>
                ) {if (response.isSuccessful){
                    val index = response.body()!!
                    val adapter = Adapter(baseContext, index)
                    timeLog.adapter = adapter
                }
                else{
                    val apiError = ErrorUtils.parseError(response)
                    Toast.makeText(this@MainActivity, apiError.message(), Toast.LENGTH_LONG).show()
                }
                }

                override fun onFailure(call: Call<List<ClockHistoryResponse>>, t: Throwable) {
                    val message = t.localizedMessage
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                }
            })
    }

}