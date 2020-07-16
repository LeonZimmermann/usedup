package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.CategoryRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository
import com.hotmail.leon.zimmermann.homeassistant.shopping.ShoppingListBuilder

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {
    val database = Firebase.firestore

    val shoppingList = ShoppingListBuilder()
        .addDiscrepancies()
        .build()
}
