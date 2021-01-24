package com.hotmail.leon.zimmermann.homeassistant.app.settings

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.preference.DialogPreference
import com.hotmail.leon.zimmermann.homeassistant.app.toTimeDisplayString
import java.io.Serializable
import java.util.*

class TimePreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs) {

  private var time: Time? = null

  init {
    summaryProvider = SimpleSummaryProvider
  }

  fun setTime(time: Time) {
    val wasBlocking = shouldDisableDependents()
    persistStringSet(setOf(time.hour.toString(), time.minute.toString()))
    this.time = time
    val isBlocking = shouldDisableDependents()
    if (isBlocking != wasBlocking) {
      notifyDependencyChange(isBlocking)
    }
    notifyChanged()
  }

  override fun onSetInitialValue(defaultValue: Any?) {
    if (defaultValue != null) {
      setTime(defaultValue as Time)
    }
  }

  override fun onSaveInstanceState(): Parcelable? {
    val superState = super.onSaveInstanceState()
    if (isPersistent) {
      return superState
    }
    return SavedState(superState, requireNotNull(time))
  }

  override fun onRestoreInstanceState(state: Parcelable?) {
    if (state == null || state.javaClass != SavedState::class.java) {
      super.onRestoreInstanceState(state)
      return
    }
    val myState = state as SavedState
    super.onRestoreInstanceState(myState.superState)
    setTime(myState.time)
  }

  private class SavedState(superState: Parcelable, val time: Time) : BaseSavedState(superState) {
    override fun writeToParcel(dest: Parcel, flags: Int) {
      super.writeToParcel(dest, flags)
      dest.writeSerializable(time)
    }
  }

  private object SimpleSummaryProvider : SummaryProvider<TimePreference> {
    override fun provideSummary(preference: TimePreference?): CharSequence {
      return preference?.time?.toString() ?: "No Time selected"
    }
  }

  data class Time(val hour: Int = 0, val minute: Int = 0) : Serializable {
    override fun toString(): String {
      val calendar = Calendar.getInstance()
      calendar[Calendar.HOUR_OF_DAY] = hour
      calendar[Calendar.MINUTE] = minute
      return calendar.toTimeDisplayString()
    }
  }
}