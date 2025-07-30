package com.example.plantbuddy.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
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

    LoadingOverlay(isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 🌿 Background animation in corner
            PlantBuddyBackground()
            // 🌿 Background animation
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)

            ) {

                Spacer(modifier = Modifier.padding(15.dp))
                Text(
                    text = "Plant Buddy 🌿",
                    style = MaterialTheme.typography.displayLarge
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
                            .width(150.dp)
                            .height(250.dp)
                            .clip(RoundedCornerShape(16.dp))
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
    }
}

@Composable
fun LeafLoadingSpinner(modifier: Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("leaf_loading.json"))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    LottieAnimation(
        composition,
        progress,
        modifier = Modifier.size(400.dp)
    )
}

@Composable
fun LoadingOverlay(isLoading: Boolean, content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                LeafLoadingSpinner(
                    modifier = Modifier.size(300.dp)
                )
            }
        }
    }
}

@Composable
fun PlantBuddyBackground() {
    val bgComposition by rememberLottieComposition(LottieCompositionSpec.Asset("bg_plant.json"))
    val bgProgress by animateLottieCompositionAsState(bgComposition, iterations = LottieConstants.IterateForever)

    Box(modifier = Modifier.fillMaxSize()) {
        //Background animation
        LottieAnimation(
            composition = bgComposition,
            progress = bgProgress,
            modifier = Modifier
                .width(240.dp)   // your intended rectangle width
                .height(320.dp)  // your intended rectangle height
                .align(Alignment.BottomEnd)
        )
    }

}

@Preview
@Composable
fun PlantBuddyScreenPreview() {
    PlantBuddyScreen()
}