package com.hotmail.leon.zimmermann.homeassistant.app.shopping

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class ShoppingViewModel @ViewModelInject constructor(
  @ApplicationContext private val context: Context,
  private val productRepository: ProductRepository,
  private val measureRepository: MeasureRepository
) : ViewModel() {

  val shoppingList: MutableLiveData<List<ShoppingProduct>> = MutableLiveData()

  val systemMessage: MutableLiveData<String> = MutableLiveData()

  fun createShoppingList() = viewModelScope.launch(Dispatchers.Default) {
    shoppingList.postValue(ShoppingListBuilder(productRepository, measureRepository)
      .addDiscrepancies()
      .build())
  }

  fun submitTransaction() = viewModelScope.launch(Dispatchers.Default) {
    try {
      shoppingList.value!!.filter { it.checked }.forEach { shoppingProduct ->
        val updatedQuantity = shoppingProduct.product.quantity + shoppingProduct.cartAmount
        productRepository.changeQuantity(shoppingProduct.product, updatedQuantity)
      }
    } catch (e: IOException) {
      systemMessage.postValue("A database error occurred")
    }
  }
}
