package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.creation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConsumptionCreationViewModel : ViewModel() {
    var descriptionString = MutableLiveData<String>()
    var instructionsString = MutableLiveData<String>()

    val showDescriptionButton: Boolean
        get() = descriptionString.value.isNullOrEmpty()
    val showInstructionsButton: Boolean
        get() = instructionsString.value.isNullOrEmpty()
}
