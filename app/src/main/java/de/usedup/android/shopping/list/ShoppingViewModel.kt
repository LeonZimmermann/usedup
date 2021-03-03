package de.usedup.android.shopping.list

import android.view.View
import androidx.lifecycle.*
import androidx.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.R
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.repositories.CategoryRepository
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import de.usedup.android.shopping.data.ShoppingList
import de.usedup.android.shopping.data.ShoppingProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
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
    ShoppingListCategoryRepresentation(categoryRepository.getCategoryForId(entry.key)!!.name,
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
