package com.lamaq.tasbeeh.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

@Composable
fun LoadImageFromUrl(url: String) {
    var image: ImageBitmap? by remember(url) { mutableStateOf(null) }

    Glide.with(LocalContext.current)
        .asBitmap()
        .load(url)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                image = resource.asImageBitmap()
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                // Not used
            }
        })

    image?.let {
        Image(
            bitmap = it,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )
    }
}