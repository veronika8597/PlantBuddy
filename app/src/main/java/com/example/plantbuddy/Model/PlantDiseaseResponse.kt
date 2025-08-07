package com.example.plantbuddy.Model

data class PlantDiseaseResponse(
    val result: DiseaseResult
)

data class DiseaseResult(
    val is_healthy: HealthStatus,
    val disease: DiseaseInfo
)

data class HealthStatus(
    val binary: Boolean,
    val probability: Double
)

data class DiseaseInfo(
    val suggestions: List<DiseaseSuggestion>
)

data class DiseaseSuggestion(
    val id: String?,
    val name: String?,
    val probability: Double,
    val treatment: TreatmentInfo?
)

data class TreatmentInfo(
    val prevention: List<String>?,
    val biological: List<String>?,
    val chemical: List<String>?
)