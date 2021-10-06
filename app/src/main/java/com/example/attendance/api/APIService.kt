package com.example.attendance.api

import com.example.attendance.model.*
import retrofit2.Call
import retrofit2.http.*

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

    @FormUrlEncoded
    @POST("/api/v1/clock-in")
    fun clockIn(
        @Header("Authorization") token: String,
        @Field("lat") lat: Double,
        @Field("long") long: Double
    ): Call<ClockInResponse>

    @FormUrlEncoded
    @PUT("/api/v1/clock-out")
    fun clockOut(
        @Header("Authorization") token: String,
        @Field("lat") lat: Double,
        @Field("long") long: Double
    ): Call<ClockOutResponse>

    @GET("/api/v1/clock-history")
    fun clockHistory(
        @Header("Authorization") token: String,
    ): Call<List<ClockHistoryResponse>>
}