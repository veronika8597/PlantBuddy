package com.example.plantbuddy.data.network.Kindwise

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KindwiseRetrofitInstance {

    private const val BASE_URL = "https://plant.id/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: PlantDiseaseApi by lazy {
        retrofit.create(PlantDiseaseApi::class.java)
    }
}