package com.example.storiee.data.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.storiee.data.local.UserPreference
import com.example.storiee.data.response.LoginResult
import com.example.storiee.view.main.MainViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    private const val BASE_URL = "https://story-api.dicoding.dev/v1/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofit.create(ApiService::class.java)
    }
}