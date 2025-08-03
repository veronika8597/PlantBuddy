package com.example.plantbuddy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.plantbuddy.R

// Set of Material typography styles to start with
val DancingScript = FontFamily(
    Font(R.font.dancing_script_regular, FontWeight.Normal),
    Font(R.font.dancing_script_bold, FontWeight.Bold),
    Font(R.font.dancing_script_semibold, FontWeight.SemiBold),
    Font(R.font.dancing_script_medium, FontWeight.Medium)
)
val Zain = FontFamily(
    Font(R.font.zain_regular, FontWeight.Normal),
    Font(R.font.zain_bold, FontWeight.Bold),
    Font(R.font.zain_extra_bold, FontWeight.ExtraBold),
    Font(R.font.zain_italic, FontWeight.Thin),
    Font(R.font.zain_light, FontWeight.Light)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = DancingScript,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 38.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = DancingScript,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = DancingScript,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = DancingScript,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Zain,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = Zain,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )
)




