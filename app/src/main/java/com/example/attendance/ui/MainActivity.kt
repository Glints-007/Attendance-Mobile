package com.example.attendance.ui

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance.R
import com.example.attendance.SharedPrefManager
import com.example.attendance.api.APIClient
import com.example.attendance.model.LogoutResponse
import com.google.android.gms.location.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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

    private lateinit var fusedLocationProvider: FusedLocationProviderClient

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
        timeLog = findViewById(R.id.timeLogRV)

        timeShow.text = getCurrentTime()
        scheduleDate.text = "${getDay()}, ${getTodayState()}"

        val name = intent.getStringExtra("name")
        username.text = name

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        logoutFab.setOnClickListener {
            logout()
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
            Log.d("Coordinate", "Lat : ${location.latitude} \nLong : ${location.longitude}")

            locatTV.text = "Your location: ${getCityName(location.latitude, location.longitude)}"
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
        Log.d("Debug:", "City: $cityName ; Country: $countryName")
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
        Log.i(TAG, "onRequestPermissionResult")
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