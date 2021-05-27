package de.usedup.android.components.base

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import de.usedup.android.R
import de.usedup.android.modules.ConnectionStateHolder
import kotlinx.android.synthetic.main.usedup_fragment.view.*

class UsedupContainer : ConstraintLayout {

  private val entryPoint: UsedupContainerEntryPoint
  private val connectionStateHolder: ConnectionStateHolder
  private val wrapper: View?

  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
    context, attrs, defStyleAttr, defStyleRes) {
    this.entryPoint = EntryPointAccessors.fromApplication(context, UsedupContainerEntryPoint::class.java)
    this.connectionStateHolder = entryPoint.getConnectionStateHolder()
    this.wrapper = inflate(context, R.layout.usedup_fragment, this)
    wrapper.bannerView.onPositiveClicked { context.startActivity(Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)) }
    wrapper.bannerView.onNegativeClicked { bannerView.visibility = View.GONE }
    findViewTreeLifecycleOwner()?.let {
      connectionStateHolder.hasConnection.observe(it) { hasConnection ->
        wrapper.bannerView.visibility = if (hasConnection) View.GONE else View.VISIBLE
      }
    }
  }

  override fun addView(child: View?) {
    return if (wrapper != null) {
      wrapper.container.addView(child)
    } else {
      super.addView(child)
    }
  }

  override fun addView(child: View?, index: Int) {
    return if (wrapper != null) {
      wrapper.container.addView(child, index)
    } else {
      super.addView(child, index)
    }
  }

  override fun addView(child: View?, width: Int, height: Int) {
    return if (wrapper != null) {
      wrapper.container.addView(child, width, height)
    } else {
      return super.addView(child, width, height)
    }
  }

  override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
    return if (wrapper != null) {
      wrapper.container.addView(child, params)
    } else {
      super.addView(child, params)
    }
  }

  override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
    return if (wrapper != null) {
      wrapper.container.addView(child, index, params)
    } else {
      super.addView(child, index, params)
    }
  }

  override fun removeView(view: View?) {
    if (wrapper != null) {
      wrapper.container.removeView(view)
    } else {
      super.removeView(view)
    }
  }

  override fun removeAllViews() {
    if (wrapper != null) {
      wrapper.container.removeAllViews()
    } else {
      super.removeAllViews()
    }
  }

  @EntryPoint
  @InstallIn(SingletonComponent::class)
  interface UsedupContainerEntryPoint {
    fun getConnectionStateHolder(): ConnectionStateHolder
  }
}