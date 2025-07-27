package com.example.plantbuddy.ViewModel

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

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

fun createImageUri(context: Context): Uri {
    val imageFile = File.createTempFile("plant_photo_", ".jpg", context.cacheDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )
}
