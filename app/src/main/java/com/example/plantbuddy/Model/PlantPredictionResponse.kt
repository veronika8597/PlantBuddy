package com.example.plantbuddy.Model

data class PlantPredictionResponse(
    val bestMatch: String,
    val results: List<PlantResult>
)

data class PlantResult(
    val score: Double,
    val species: Species
)

data class Species(
    val scientificName: String,
    val commonNames: List<String>
)