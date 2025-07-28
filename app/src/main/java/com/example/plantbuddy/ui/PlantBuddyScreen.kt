package com.example.plantbuddy.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.plantbuddy.ViewModel.PlantViewModel


@Composable
fun PlantBuddyScreen(plantViewModel: PlantViewModel = viewModel()){

    val isLoading by plantViewModel.isLoading.collectAsState()

    val selectedUri by plantViewModel.selectedImageUri.collectAsState()
    val diagnosis by plantViewModel.diagnosis.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        plantViewModel.updateImage(uri)
    }

    val context = LocalContext.current
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraImageUri != null) {
            plantViewModel.updateImage(cameraImageUri)
        }
    }


    Column(modifier = Modifier.
    fillMaxSize()
        .padding(16.dp)
        .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = "🌿 Plant Buddy",
            style = MaterialTheme.typography.headlineMedium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { launcher.launch("image/*") }) {
                Text("\uD83D\uDCC1 Pick from gallery")
            }
            Button(onClick = {
                cameraImageUri = plantViewModel.createImageUri(context)
                takePictureLauncher.launch(cameraImageUri!!)
            }) {
                Text("\uD83D\uDCF7 Take a photo")
            }
        }

            selectedUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Selected Plant Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }

            Text(text = diagnosis)

        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = "Powered by PlantNet",
            style = MaterialTheme.typography.bodySmall
        )

        Button(onClick = {
            plantViewModel.diagnosePlant(context)
        }) {
            Text("🔍 Diagnose")
        }
    }
}

@Preview
@Composable
fun PlantBuddyScreenPreview() {
    PlantBuddyScreen()
}