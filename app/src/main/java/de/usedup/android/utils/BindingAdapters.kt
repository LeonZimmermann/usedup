package de.usedup.android.utils

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
  if (!url.isNullOrBlank()) {
    Glide.with(view.context)
      .load(url)
      .circleCrop()
      .into(view)
  }
}

@BindingAdapter("src")
fun loadImage(view: ImageView, bitmap: Bitmap?) {
  if (bitmap != null) {
    Glide.with(view.context)
      .load(bitmap)
      .circleCrop()
      .into(view)
  }
}