package com.example.attendance.api

import com.example.attendance.model.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface APIService {
    @FormUrlEncoded
    @POST("/api/v1/register")
    fun registUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegistResponse>

    @FormUrlEncoded
    @POST("/api/v1/login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @POST("/api/v1/logout")
    fun logoutUser(
        @Header("Authorization") token: String
    ): Call<LogoutResponse>

    @FormUrlEncoded
    @POST("/api/v1/forgot")
    fun forgotPass(
        @Field("email") email: String,
    ): Call<ForgotResponse>

    @FormUrlEncoded
    @POST("/api/v1/reset")
    fun resetPass(
        @Field("email") email: String,
        @Field("token") token: String,
        @Field("password") password: String,
        @Field("confirm_password") confirm_password: String
    ): Call<ResetResponse>
}