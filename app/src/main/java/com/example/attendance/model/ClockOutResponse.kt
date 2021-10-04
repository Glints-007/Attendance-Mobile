package com.example.attendance.model
import com.google.gson.annotations.SerializedName

data class ClockOutResponse(
    @SerializedName("errors")
    val errors: String,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)