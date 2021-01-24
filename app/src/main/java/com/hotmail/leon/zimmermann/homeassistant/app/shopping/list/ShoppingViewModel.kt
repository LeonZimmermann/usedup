package com.hotmail.leon.zimmermann.homeassistant.app.shopping.list

import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingList
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.CategoryRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingViewModel @ViewModelInject constructor(
  private val productRepository: ProductRepository,
  private val categoryRepository: CategoryRepository
) : ViewModel() {

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

  fun onConfirmButtonPressed(view: View) = viewModelScope.launch(Dispatchers.Default) {
    shoppingCart.forEach { shoppingProduct ->
      launch(Dispatchers.IO) {
        productRepository.changeQuantity(shoppingProduct.product,
          shoppingProduct.product.quantity + shoppingProduct.cartAmount)
      }
    }
    launch(Dispatchers.Main) {
      Navigation.findNavController(view).apply {
        navigate(R.id.action_global_overview_fragment)
        popBackStack(R.id.action_global_overview_fragment, false)
      }
    }
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
