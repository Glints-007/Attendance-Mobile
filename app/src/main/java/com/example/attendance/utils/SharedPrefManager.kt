package com.example.attendance.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.attendance.model.LoginResponse

class SharedPrefManager(context: Context) {
    private var sharedPref: SharedPreferences = context.getSharedPreferences("Attendance", Context.MODE_PRIVATE)
    private lateinit var editor: SharedPreferences.Editor

    fun saveUser(userLogin: LoginResponse.Content){
        editor = sharedPref.edit()
        userLogin.status_code?.let { editor.putInt("status_code", it) }
        editor.putString("access_token", userLogin.access_token)
        editor.putString("token_type", userLogin.token_type)
        editor.putString("name", userLogin.name)
        editor.putInt("office_id", userLogin.office_id!!)
        editor.putString("role", userLogin.role)
        editor.putBoolean("logged", true)
        editor.apply()
    }

    var token
        get() = sharedPref.getString("access_token", null)
        set(value) = sharedPref.edit().putString("access_token", value).apply()

    fun clear(){
        val prefEdit: SharedPreferences.Editor = sharedPref.edit()
        prefEdit.clear()
        prefEdit.commit()
    }
}