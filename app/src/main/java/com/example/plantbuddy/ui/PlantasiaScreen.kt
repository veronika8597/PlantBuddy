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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.plantbuddy.R
import com.example.plantbuddy.ViewModel.PlantViewModel


@Composable
fun PlantasiaScreen(plantViewModel: PlantViewModel = viewModel()){

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
        Box(modifier = Modifier.fillMaxSize())
        {
            PlantasiaBackground()
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp)
                        .padding(start = 16.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plantasia_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .fillMaxWidth(0.65f), // Adjust fraction as needed
                    )
                }


                Card(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                        ,
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xA6FFFFFF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),

                    ) {

                    Spacer(modifier = Modifier.padding(15.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(10.dp)) {
                        Button(
                            onClick = { launcher.launch("image/*") },
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 8.dp
                            ),
                            modifier = Modifier
                                .height(40.dp)
                                .weight(1f) // this helps buttons share space evenly in Row
                        ) {
                            Text("\uD83D\uDCC1 Pick from gallery")

                        }
                        Button(
                            onClick = {
                                cameraImageUri = plantViewModel.createImageUri(context)
                                takePictureLauncher.launch(cameraImageUri!!)
                            },
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 8.dp
                            ),
                            modifier = Modifier
                                .height(40.dp)
                                .weight(1f) // this helps buttons share space evenly in Row
                        ) {
                            Text("\uD83D\uDCF7 Take a photo")
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(360.dp)
                                .shadow(8.dp, RoundedCornerShape(16.dp)), // depth
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

                            ) {

                            Box(modifier = Modifier.padding(8.dp)) {
                                selectedUri?.let { uri ->
                                    Image(
                                        painter = rememberAsyncImagePainter(uri),
                                        contentDescription = "Selected Plant Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(300.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    )
                                }

                            }


                            UnderlineLabelText(
                                fullLine = diagnosis,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                        }
                    }


                    Row(modifier = Modifier.padding(10.dp)) {
                        Button(
                            onClick = {
                                plantViewModel.diagnoseFull(context)
                            },
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 8.dp
                            ),
                            modifier = Modifier
                                .height(40.dp)
                                .weight(1f) // this helps buttons share space evenly in Row
                        ) {
                            Text("🔍 Diagnose")
                        }
                    }

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
        modifier = Modifier
            .size(300.dp)
            .offset(x = 12.dp)

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
                    modifier = Modifier
                        .size(300.dp)
                )
            }
        }
    }
}

@Composable
fun PlantasiaBackground() {

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.plantasia_bg),
            contentDescription = "App Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = "Powered by PlantNet",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 13.dp)
        )
    }

}

@Composable
fun UnderlineLabelText(fullLine: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        fullLine.lines().forEach { line ->
            val parts = line.split(":", limit = 2)
            if (parts.size == 2) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(parts[0] + ":")
                        }
                        append(parts[1])
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            } else {
                Text(
                    text = line,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun PlantasiaScreenPreview() {
    PlantasiaScreen()
}