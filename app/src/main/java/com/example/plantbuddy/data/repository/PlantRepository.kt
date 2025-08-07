package com.example.plantbuddy.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.plantbuddy.BuildConfig.PLANT_NET_API_KEY
import com.example.plantbuddy.Model.PlantDiseaseResponse
import com.example.plantbuddy.data.network.Kindwise.KindwiseRetrofitInstance
import com.example.plantbuddy.data.network.PlantNet.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


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

            val apiKey = PLANT_NET_API_KEY
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


    suspend fun diagnoseDisease(context: Context, uri: Uri): Result<String> {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)!!
            val requestBody = inputStream.readBytes()
                .toRequestBody("image/*".toMediaTypeOrNull())

            val multipart = MultipartBody.Part.createFormData(
                "images", "plant.jpg", requestBody
            )

            val apiKey = "Bq9p9EaPARoxRShGa35UhY1C42CJ06x8lHETR6z3XVYZ1S3mGr" // replace this
            val response = KindwiseRetrofitInstance.api.diagnoseDisease(
                listOf(multipart),
                apiKey
            )

            val body = response.body()

            if (response.isSuccessful && body != null) {
                val isHealthy = body.result.is_healthy.binary

                return if (isHealthy) {
                    Result.success("Your plant is healthy 🌿")
                } else {
                    val disease = body.result.disease.suggestions.firstOrNull()
                    val name = disease?.name ?: "Unknown issue"

                    // 🔧 Manual treatment suggestions for common non-disease issues
                    val manualTreatments = mapOf(
                        "light excess" to """
            💡 Try this to prevent it - Move the plant to indirect light
            🌿 Natural remedy - Shade with sheer curtains
            💧 Tip - Avoid direct afternoon sun
        """.trimIndent(),
                        "excess water" to """
            💡 Try this to prevent it - Let soil dry out between waterings
            🌿 Natural remedy - Repot with well-draining soil
            💧 Tip - Ensure pot has drainage holes
        """.trimIndent()
                    )

                    val lowerName = name.lowercase()
                    val manualTreatmentText = manualTreatments.entries.firstOrNull { lowerName.contains(it.key) }?.value

                    val treatmentText = if (manualTreatmentText != null) {
                        "Treatment:\n$manualTreatmentText"
                    } else {
                        val treatmentInfo = disease?.treatment
                        buildString {
                            append("Treatment:\n")
                            treatmentInfo?.prevention?.firstOrNull()?.let {
                                append("💡 Try this to prevent it - $it\n")
                            }
                            treatmentInfo?.biological?.firstOrNull()?.let {
                                append("🌿 Natural remedy - $it\n")
                            }
                            treatmentInfo?.chemical?.firstOrNull()?.let {
                                append("💊 Chemical option - $it\n")
                            }
                            if (isBlank()) append("No treatment information available.")
                        }
                    }

                    Result.success("Disease detected: $name\n\n$treatmentText".trim())
                }
            } else {
                Result.failure(Exception("Disease check failed: ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}