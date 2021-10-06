package com.example.attendance.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ClockHistoryResponse {
    @SerializedName("clock_in")
    @Expose
    val clockIn: String? = null

    @SerializedName("clock_in_lat")
    @Expose
    val clockInLat: String? = null

    @SerializedName("clock_in_long")
    @Expose
    val clockInLong: String? = null

    @SerializedName("clock_out")
    @Expose
    val clockOut: String? = null

    @SerializedName("clock_out_lat")
    @Expose
    val clockOutLat: String? = null

    @SerializedName("clock_out_long")
    @Expose
    val clockOutLong: String? = null

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null

    @SerializedName("id")
    @Expose
    val id: Int? = null

    @SerializedName("total_working_hours")
    @Expose
    val totalWorkingHours: String? = null

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null

    @SerializedName("user_id")
    @Expose
    val userId: String? = null
}
