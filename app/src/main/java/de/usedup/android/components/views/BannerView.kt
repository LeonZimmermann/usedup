package de.usedup.android.components.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.getStringOrThrow
import de.usedup.android.R
import kotlinx.android.synthetic.main.banner_view.view.*

class BannerView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

  private val view = inflate(context, R.layout.banner_view, this)

  init {
    context.theme.obtainStyledAttributes(attrs, R.styleable.BannerView, defStyleAttr, defStyleRes).apply {
      try {
        view.message_tv.text = getString(R.styleable.BannerView_banner_text)
        view.icon_image.setImageDrawable(
          getDrawable(R.styleable.BannerView_banner_icon) ?: ResourcesCompat.getDrawable(context.resources,
            R.drawable.info_icon, null))

      } finally {
        recycle()
      }
    }
  }

  fun onPositiveClicked(function: (View) -> Unit): BannerView {
    view.positive_button.setOnClickListener(function)
    return this
  }

  fun onNegativeClicked(function: (View) -> Unit): BannerView {
    view.negative_button.setOnClickListener(function)
    return this
  }
}