package com.example.attendance.model
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("errors")
    @Expose
    val errors: String? = null

    @SerializedName("msg")
    @Expose
    val msg: String? = null

    @SerializedName("status")
    @Expose
    val status: String? = null

    @SerializedName("content")
    @Expose
    val content: Content? = null

    class Content{
        @SerializedName("status_code")
        @Expose
        var status_code: Int? = null

        @SerializedName("access_token")
        @Expose
        var access_token: String? = null

        @SerializedName("token_type")
        @Expose
        var token_type: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("office_id")
        @Expose
        var office_id: Int? = null

        @SerializedName("role")
        @Expose
        var role: String? = null
    }
}
