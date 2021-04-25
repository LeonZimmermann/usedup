package de.usedup.android.components.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.preference.PreferenceManager
import de.usedup.android.R
import kotlinx.android.synthetic.main.infobox_view.view.*

class InfoboxView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
  init {
    val view = inflate(context, R.layout.infobox_view, this)
    view.visibility = if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("display_info", true)) {
      View.VISIBLE
    } else {
      View.GONE
    }
    context.theme.obtainStyledAttributes(attrs, R.styleable.InfoboxView, defStyleAttr, defStyleRes).apply {
      try {
        view.info_text.text = getString(R.styleable.InfoboxView_text)
      } finally {
        recycle()
      }
    }
  }
}