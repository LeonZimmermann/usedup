package com.hotmail.leon.zimmermann.homeassistant.app.shopping

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {
    val database = Firebase.firestore
    private val shoppingListProcessor = ShoppingListProcessor(this)

    val shoppingList = MutableLiveData(
        ShoppingListBuilder()
            .addDiscrepancies()
            .build()
    )

    val systemMessage = MutableLiveData("")

    fun submitTransaction() {
        viewModelScope.launch {
            shoppingList.value?.let { shoppingListProcessor.process(it) }
        }
    }
}
