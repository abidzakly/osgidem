package org.d3ifcool.medisgosh.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.d3ifcool.medisgosh.R

// App's Font Family
val Archivo = FontFamily(
    Font(R.font.archivo_regular, FontWeight.Normal),
    Font(R.font.archivo_medium, FontWeight.Medium),
    Font(R.font.archivo_semibold, FontWeight.SemiBold),
    Font(R.font.archivo_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.archivo_extra_bold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.archivo_black, FontWeight.Black, FontStyle.Normal),
    Font(R.font.archivo_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.archivo_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.archivo_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
)

val AdlamDisplay = FontFamily(
    Font(R.font.adlamdisplay_regular, FontWeight.Normal),
)

val Candal = FontFamily(
    Font(R.font.candal_regular, FontWeight.Normal),
)

val AppTypography = TextStyle(
    fontWeight = FontWeight.W600,
    fontFamily = Archivo,
    fontSize = 14.sp,
)

val AppTypography2 = Typography(
    bodySmall = TextStyle(
        fontFamily = Archivo,
        fontSize = 12.sp,
        color = Color.Black,
    ),
    bodyMedium = TextStyle(
        fontFamily = Archivo,
        fontSize = 14.sp,
        color = Color.Black,
    ),
    bodyLarge = TextStyle(
        fontFamily = Archivo,
        fontSize = 16.sp,
        color = Color.Black,
    ),
    labelSmall = TextStyle(
        fontFamily = Archivo,
        fontSize = 18.sp,
        color = Color.Black,
    ),
    labelMedium = TextStyle(
        fontFamily = Archivo,
        fontSize = 20.sp,
        color = Color.Black,
    ),
    labelLarge = TextStyle(
        fontFamily = Archivo,
        fontSize = 22.sp,
        color = Color.Black,
    ),
    displaySmall = TextStyle(
        fontFamily = Archivo,
        fontSize = 24.sp,
        color = Color.Black,
    ),
    displayMedium = TextStyle(
        fontFamily = Archivo,
        fontSize = 26.sp,
        color = Color.Black,
    ),
    displayLarge = TextStyle(
        fontFamily = Archivo,
        fontSize = 28.sp,
        color = Color.Black,
    ),
)