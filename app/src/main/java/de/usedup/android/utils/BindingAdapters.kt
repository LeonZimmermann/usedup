package de.usedup.android.utils

import android.graphics.Bitmap
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.usedup.android.household.HouseholdMemberRepresentation

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

@BindingAdapter("android:layout_marginTop")
fun setLayoutMarginTop(view: TextView, marginTop: Int) {
  if (view.layoutParams is ViewGroup.MarginLayoutParams) {
    val p = view.layoutParams as ViewGroup.MarginLayoutParams
    p.setMargins(p.leftMargin, marginTop, p.rightMargin, p.bottomMargin)
    view.requestLayout()
  }
}

@BindingAdapter("android:text")
fun setTextWithRole(view: TextView, role: HouseholdMemberRepresentation.Role?) {
  view.text = if (role != null) view.context.getString(role.ressourceId) else null
}