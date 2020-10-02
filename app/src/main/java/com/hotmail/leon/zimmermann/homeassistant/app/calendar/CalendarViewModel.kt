package com.hotmail.leon.zimmermann.homeassistant.app.calendar

import android.app.Application
import android.graphics.RectF
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hotmail.leon.zimmermann.homeassistant.components.views.CalendarWeekView

class CalendarViewModel(application: Application) : AndroidViewModel(application), CalendarWeekView.OnScrollListener,
  CalendarWeekView.OnDateTimeSelectedListener, CalendarWeekView.OnEntryClickedListener {

  var toastMessage = MutableLiveData<String>()

  var addButtonVisible = MutableLiveData<Boolean>(true)

  fun onAddButtonPressed() {

  }

  override fun onScroll(scrollOffset: Float) {
    addButtonVisible.postValue(scrollOffset < SCROLL_THRESHOLD)
  }

  override fun onEntryClicked(entry: CalendarWeekView.Entry, rect: RectF, index: Int) {
    toastMessage.postValue("Entry clicked: $entry $rect $index")
  }

  override fun onDateTimeSelected(weekday: Int, hour: Int, minute: Int) {
    toastMessage.postValue("DateTime selected: $weekday - $hour:$minute")
  }

  companion object {
    private const val SCROLL_THRESHOLD = 20f
  }
}
