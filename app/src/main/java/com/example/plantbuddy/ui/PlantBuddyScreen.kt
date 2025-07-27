package com.example.plantbuddy.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter

import com.example.plantbuddy.ViewModel.PlantViewModel

@Composable
fun PlantBuddyScreen(plantViewModel: PlantViewModel = viewModel()){

    val selectedUri by plantViewModel.selectedImageUri.collectAsState()
    val diagnosis by plantViewModel.diagnosis.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        plantViewModel.updateImage(uri)
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

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Pick a plant photo")
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

        Text(text = diagnosis)
    }

}

@Preview
@Composable
fun PlantBuddyScreenPreview() {
    PlantBuddyScreen()
}