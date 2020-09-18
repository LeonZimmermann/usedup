package com.hotmail.leon.zimmermann.homeassistant.components.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.hotmail.leon.zimmermann.homeassistant.components.R
import kotlinx.android.synthetic.main.infobox_view.view.*

class InfoboxView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    init {
        val view = inflate(context, R.layout.infobox_view, this)
        context.theme.obtainStyledAttributes(attrs, R.styleable.InfoboxView, defStyleAttr, defStyleRes).apply {
            try {
                view.info_text.text = getString(R.styleable.InfoboxView_text)
            } finally {
                recycle()
            }
        }
    }
}