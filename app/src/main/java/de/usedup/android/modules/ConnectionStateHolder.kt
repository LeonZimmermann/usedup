package de.usedup.android.modules

import androidx.lifecycle.MutableLiveData

object ConnectionStateHolder {
  val hasConnection: MutableLiveData<Boolean> = MutableLiveData(false)
}