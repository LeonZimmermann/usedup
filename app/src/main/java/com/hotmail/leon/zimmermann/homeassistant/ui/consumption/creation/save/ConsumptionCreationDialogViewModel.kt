package com.hotmail.leon.zimmermann.homeassistant.ui.consumption.creation.save

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConsumptionCreationDialogViewModel: ViewModel() {
    var name = MutableLiveData<String>("")
}