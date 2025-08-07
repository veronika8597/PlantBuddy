package com.example.plantbuddy.data.network.PlantNet

import com.example.plantbuddy.Model.PlantPredictionResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface PlantIdentificationApi {

    @Multipart
    @POST("v2/identify/all") // or whatever your endpoint is
    suspend fun uploadImage(
        @Part images: List<MultipartBody.Part>,
        @Query("api-key") apiKey: String
    ): Response<PlantPredictionResponse>



}