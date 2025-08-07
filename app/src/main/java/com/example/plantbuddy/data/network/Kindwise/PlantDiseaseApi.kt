package com.example.plantbuddy.data.network.Kindwise

import com.example.plantbuddy.Model.PlantDiseaseResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.*

interface PlantDiseaseApi {
    @Multipart
    @POST("api/v3/identification")
    suspend fun diagnoseDisease(
        @Part images: List<MultipartBody.Part>,
        @Header("Api-Key") apiKey: String,
        @Part("health") healthMode: RequestBody = "all".toRequestBody("text/plain".toMediaTypeOrNull())
    ): Response<PlantDiseaseResponse>
}