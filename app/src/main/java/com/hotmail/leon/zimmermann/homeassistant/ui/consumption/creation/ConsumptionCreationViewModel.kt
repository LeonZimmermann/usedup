package com.hotmail.leon.zimmermann.homeassistant.ui.consumption.creation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConsumptionCreationViewModel : ViewModel() {
    var descriptionString = MutableLiveData<String>()
    var instructionsString = MutableLiveData<String>()
}
