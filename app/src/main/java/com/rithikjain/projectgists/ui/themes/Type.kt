package com.rithikjain.projectgists.ui.themes

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.rithikjain.projectgists.R

val circularStd = FontFamily(
  Font(R.font.circularstd_black, FontWeight.Black),
  Font(R.font.circularstd_medium, FontWeight.Medium),
  Font(R.font.circularstd_bold, FontWeight.Bold),
  Font(R.font.circularstd_book, FontWeight(350))
)

val typography = Typography(
  defaultFontFamily = circularStd
)