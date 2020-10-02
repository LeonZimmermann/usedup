package com.hotmail.leon.zimmermann.homeassistant.app.shopping

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {

  val shoppingList: MutableLiveData<List<ShoppingProduct>> = MutableLiveData(
    ShoppingListBuilder()
      .addDiscrepancies()
      .build()
  )

  val systemMessage: MutableLiveData<String> = MutableLiveData()

  fun submitTransaction() = viewModelScope.launch(Dispatchers.Default) {
    try {
      shoppingList.value!!.filter { it.checked }.forEach { shoppingProduct ->
        val updatedQuantity = shoppingProduct.product.quantity + shoppingProduct.cartAmount
        ProductRepository.changeQuantity(shoppingProduct.product, updatedQuantity)
      }
    } catch (e: IOException) {
      systemMessage.postValue("A database error occurred")
    }
  }
}
