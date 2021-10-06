package com.example.attendance.api

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object APIClient {
    private const val BASE_URL = "https://floating-journey-97236.herokuapp.com"
    private val gson = GsonBuilder().setLenient().create()

    private val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val request: Request = chain.request()
                return chain.proceed(request)
            }
        })
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30,TimeUnit.SECONDS)
        .build()

    private var retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()
    fun retrofit(): Retrofit{
            return retrofit
        }

    val service: APIService
        get() = retrofit().create(APIService::class.java)
}