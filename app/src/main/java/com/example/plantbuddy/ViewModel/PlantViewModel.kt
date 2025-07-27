package com.example.plantbuddy.ViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlantViewModel : ViewModel()  {

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri = _selectedImageUri.asStateFlow()

    private val _diagnosis = MutableStateFlow("No photo selected")
    val diagnosis = _diagnosis.asStateFlow()

    fun updateImage(uri: Uri?) {
        _selectedImageUri.value = uri
        _diagnosis.value = if (uri != null) {
            "Image loaded. (Diagnosis soon!)"
        } else {
            "No image selected"
        }
    }
}