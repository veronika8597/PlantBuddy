package com.example.plantbuddy.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Builds and provides a single Retrofit instance for calling the PlantNet API.
// Includes base URL and JSON converter, and exposes the PlantIdentificationApi interface.
object RetrofitInstance {

    private const val BASE_URL = "https://my-api.plantnet.org/"

    private val retrofit: Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: PlantIdentificationApi by lazy {
        retrofit.create(PlantIdentificationApi::class.java)
    }

}