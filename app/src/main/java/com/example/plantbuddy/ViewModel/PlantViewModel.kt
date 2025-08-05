package com.example.plantbuddy.ViewModel

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantbuddy.data.repository.PlantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class PlantViewModel : ViewModel()  {

    private val repository = PlantRepository()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri = _selectedImageUri.asStateFlow()

    private val _diagnosis = MutableStateFlow("No photo selected")
    val diagnosis = _diagnosis.asStateFlow()

    fun updateImage(uri: Uri?) {
        _selectedImageUri.value = uri
        _diagnosis.value = if (uri != null) {
            "Image loaded. Ready to diagnose!"
        } else {
            "No image selected"
        }
    }

// Creates a writable Uri pointing to a temporary image file in cache.
// This Uri can be safely shared with the camera or other apps via FileProvider.
// Use this when capturing photos with ACTION_IMAGE_CAPTURE — the image will be saved directly to this Uri.
// ✅ Suitable for upload after camera capture
    fun createImageUri(context: Context): Uri {
        val imageFile = File.createTempFile("plant_photo_", ".jpg", context.cacheDir)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            imageFile
        )
    }


    fun diagnosePlant(context: Context) {
        val uri = _selectedImageUri.value ?: return

        viewModelScope.launch {
            _isLoading.value = true
            _diagnosis.value = "Diagnosing..."


            val result = repository.uploadImage(context, uri)

            _diagnosis.value = result.fold(
                onSuccess = { plantName -> "Plant name: $plantName"},
                onFailure = { error -> "Error: ${error.message}" }
            )

            _isLoading.value = false
        }
    }

}
