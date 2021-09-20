package com.example.attendance.model
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class FogotResponse {
    @SerializedName("data")
    @Expose
    val `data`: Data? = null

    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    class Data{
        @SerializedName("message")
        @Expose
        val message: String? = null
    }
}