package com.rithikjain.projectgists.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rithikjain.projectgists.ui.themes.EzGistsTheme
import com.rithikjain.projectgists.ui.themes.lightPurple
import com.rithikjain.projectgists.ui.themes.vividPink

@Composable
fun GistItem(title: String, body: String?) {
  Card(
    Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 8.dp),
    backgroundColor = lightPurple,
    contentColor = Color.White,
    shape = RoundedCornerShape(10.dp)
  ) {
    Column {
      Text(
        text = title,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.h6.copy(color = vividPink)
      )
      Text(
        text = if (body.isNullOrBlank()) "No description" else body,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        fontWeight = FontWeight(350),
      )
    }
  }
}

@Preview
@Composable
fun ItemPreview() {
  EzGistsTheme {
    GistItem(title = "Hi there", body = null)
  }
}