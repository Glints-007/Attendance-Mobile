package com.example.attendance.model
import com.google.gson.annotations.SerializedName


data class LogoutResponse(
    @SerializedName("content")
    val content: Any,
    @SerializedName("errors")
    val errors: Any,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)