package com.hotmail.leon.zimmermann.homeassistant.app.ui.shopping

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.ShoppingListBuilder

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {
    val database = Firebase.firestore

    val shoppingList = ShoppingListBuilder()
        .addDiscrepancies()
        .build()
}
