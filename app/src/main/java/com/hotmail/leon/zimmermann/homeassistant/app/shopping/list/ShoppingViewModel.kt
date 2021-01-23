package com.hotmail.leon.zimmermann.homeassistant.app.shopping.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingList
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.CategoryRepository

class ShoppingViewModel @ViewModelInject constructor(private val categoryRepository: CategoryRepository) : ViewModel() {

  private val shoppingList: MutableLiveData<ShoppingList> = MutableLiveData()
  private val shoppingCart: MutableSet<ShoppingProduct> = mutableSetOf()

  val shoppingListCategories: LiveData<Set<ShoppingListCategoryRepresentation>> =
    Transformations.map(shoppingList) { shoppingList ->
      shoppingList.shoppingProducts
        .groupBy { it.product.categoryId }
        .map(::mapToShoppingListCategoryRepresentation)
        .toSet()
    }

  private fun mapToShoppingListCategoryRepresentation(entry: Map.Entry<Id, List<ShoppingProduct>>) =
    ShoppingListCategoryRepresentation(categoryRepository.getCategoryForId(entry.key).name,
      entry.value.map { ShoppingListElementRepresentation(it.product, it.cartAmount, false) }.toSet())

  fun initShoppingList(shoppingList: ShoppingList) {
    this.shoppingList.postValue(shoppingList)
  }

  val onCheckButtonPressedCallback = object : ShoppingListElementRecyclerAdapter.Callback {
    override fun onCheckButtonPressed(shoppingListElementRepresentation: ShoppingListElementRepresentation) {
      val shoppingProduct =
        ShoppingProduct(shoppingListElementRepresentation.product, shoppingListElementRepresentation.cartAmount)
      if (shoppingListElementRepresentation.checked) {
        shoppingCart.add(shoppingProduct)
      } else {
        shoppingCart.remove(shoppingProduct)
      }
    }
  }
}
