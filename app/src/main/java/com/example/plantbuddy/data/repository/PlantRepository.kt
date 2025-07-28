package com.example.plantbuddy.data.repository

import android.content.Context
import android.net.Uri
import com.example.plantbuddy.Model.PlantPredictionResponse
import com.example.plantbuddy.data.network.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response


class PlantRepository {

    private fun uriToMultipart(context: Context, uri: Uri): MultipartBody.Part {

        val inputStream = context.contentResolver.openInputStream(uri)!!
        val requestBody = inputStream.readBytes()
            .toRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(
            "image", "plant.jpg", requestBody
        )
    }

    suspend fun uploadImage(context: Context, uri: Uri): Result<String> {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)!!
            val requestBody = inputStream.readBytes()
                .toRequestBody("image/*".toMediaTypeOrNull())

            val multipart = MultipartBody.Part.createFormData(
                "images", // must be plural: "images" as per PlantNet API docs
                "plant.jpg",
                requestBody
            )

            val apiKey = "2b10M1th5Tf2kQgSBMgf6dZe"
            val response = RetrofitInstance.api.uploadImage(mutableListOf(multipart), apiKey)
            val prediction = response.body()

            if (response.isSuccessful && prediction != null) {
                val commonName = prediction.results.firstOrNull()
                    ?.species?.commonNames?.firstOrNull() ?: prediction.bestMatch

                Result.success(commonName)
            } else {
                Result.failure(Exception("Upload failed: ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}