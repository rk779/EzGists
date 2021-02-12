package com.rithikjain.projectgists.ui.widgets

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

@Composable
fun NetworkImage(
  url: String,
  modifier: Modifier = Modifier
) {
  var image by remember { mutableStateOf<ImageBitmap?>(null) }
  var drawable by remember { mutableStateOf<Drawable?>(null) }
  val context = LocalContext.current

  DisposableEffect(url) {
    val glide = Glide.with(context)
    val target = object : CustomTarget<Bitmap>() {
      override fun onLoadCleared(placeholder: Drawable?) {
        image = null
        drawable = placeholder
      }

      override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
        image = bitmap.asImageBitmap()
      }
    }

    glide.asBitmap().load(url).into(target)

    onDispose {
      image = null
      drawable = null
      glide.clear(target)
    }
  }

  val theImage = image
  val theDrawable = drawable
  if (theImage != null) {
    Box(
      modifier.clip(RoundedCornerShape(50)),
      contentAlignment = Alignment.Center,
    ) {
      Image(bitmap = theImage, contentDescription = null)
    }

  } else if (theDrawable != null) {
    Canvas(modifier = modifier) {
      drawIntoCanvas { canvas ->
        theDrawable.draw(canvas.nativeCanvas)
      }
    }
  }
}