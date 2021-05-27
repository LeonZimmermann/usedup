package de.usedup.android.shopping.preview

import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import androidx.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.R
import de.usedup.android.datamodel.api.objects.PlannerItem
import de.usedup.android.datamodel.api.objects.Product
import de.usedup.android.datamodel.api.repositories.MealRepository
import de.usedup.android.datamodel.api.repositories.MeasureRepository
import de.usedup.android.datamodel.api.repositories.PlannerRepository
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import de.usedup.android.shopping.data.ShoppingProduct
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListPreviewViewModel @Inject constructor(
  private val productRepository: ProductRepository,
  mealRepository: MealRepository,
  private val plannerRepository: PlannerRepository,
  measureRepository: MeasureRepository) : ViewModel() {

  private val shoppingListPreviewGenerator = ShoppingListPreviewGenerator(mealRepository)
  private val mapper = ShoppingListPreviewToShoppingListMapper(productRepository, measureRepository)

  private val mutableShoppingListPreview = MutableLiveData<ShoppingListPreview>()
  val shoppingListPreview: LiveData<ShoppingListPreview> = Transformations.map(mutableShoppingListPreview) { it }

  val addAdditionalProductShoppingProduct: MutableLiveData<ShoppingProduct?> = MutableLiveData(null)
  val editAdditionalProductShoppingProduct: MutableLiveData<ShoppingProduct?> = MutableLiveData(null)

  // Dialog
  var productNames: LiveData<List<String>> =
    Transformations.map(
      productRepository.getAllProductsLiveData(viewModelScope)) { products -> products.map { it.name } }
  var shoppingProductDialog: MutableLiveData<ShoppingProductDialog?> = MutableLiveData(null)
  var dialogEditShoppingProduct: ShoppingProduct? = null

  val productDiscrepancyEmpty: MutableLiveData<Boolean> = MutableLiveData(false)
  val mealsEmpty: MutableLiveData<Boolean> = MutableLiveData(false)

  fun initShoppingListPreview() {
    viewModelScope.launch(Dispatchers.IO) {
      Single.zip(listOf(productRepository.getAllProducts(), plannerRepository.getAllPlannerItems())) {
        Pair(it[0] as Set<Product>, it[1] as List<PlannerItem>)
      }.onErrorComplete().subscribe { (products, plannerItems) ->
        viewModelScope.launch(Dispatchers.IO) {
          val shoppingListPreview =
            shoppingListPreviewGenerator.generateShoppingListPreview(products, plannerItems)
          mutableShoppingListPreview.postValue(shoppingListPreview)
          productDiscrepancyEmpty.postValue(shoppingListPreview.productDiscrepancyList.isEmpty())
          mealsEmpty.postValue(shoppingListPreview.mealList.isEmpty())
        }
      }
    }
  }

  fun onGoShoppingButtonClicked(view: View) = viewModelScope.launch(Dispatchers.Main) {
    val mapResult = async(Dispatchers.Default) { mapper.mapPreviewToShoppingList(shoppingListPreview.value!!) }
    Navigation.findNavController(view)
      .navigate(R.id.action_shopping_list_preview_fragment_to_shopping_list_fragment, bundleOf(
        "shoppingList" to mapResult.await()
      ))
  }

  fun onAddProductButtonClicked() {
    dialogEditShoppingProduct = null
    shoppingProductDialog.postValue(
      ShoppingProductDialog(R.string.add_additional_product, object : ShoppingProductDialog.Callback {
        override fun onResult(shoppingProduct: ShoppingProduct) {
          val shoppingList = requireNotNull(mutableShoppingListPreview.value)
          if (shoppingList.additionalProductList.contains(shoppingProduct)) {
            shoppingList.additionalProductList.first { it == shoppingProduct }.cartAmount = shoppingProduct.cartAmount
            editAdditionalProductShoppingProduct.postValue(shoppingProduct)
          } else {
            shoppingList.additionalProductList.add(shoppingProduct)
            addAdditionalProductShoppingProduct.postValue(shoppingProduct)
          }
          mutableShoppingListPreview.postValue(shoppingList)
        }
      }))
  }

  val additionProductRecyclerAdapterCallback = object : AdditionalProductRecyclerAdapter.Callback {
    override fun onEditButtonClicked(adapter: AdditionalProductRecyclerAdapter, index: Int, view: View,
      additionalProductRepresentation: AdditionalProductRepresentation) {
      dialogEditShoppingProduct = additionalProductRepresentation.data
      shoppingProductDialog.postValue(
        ShoppingProductDialog(R.string.change_cart_amount_for_product, object : ShoppingProductDialog.Callback {
          override fun onResult(shoppingProduct: ShoppingProduct) {
            adapter.replaceAdditionalProduct(AdditionalProductRepresentation(shoppingProduct))
            val shoppingList = mutableShoppingListPreview.value!!
            shoppingList.additionalProductList.first { it == shoppingProduct }.cartAmount =
              shoppingProduct.cartAmount
            mutableShoppingListPreview.postValue(shoppingList)
          }
        }))
    }

    override fun onRemoveButtonClicked(adapter: AdditionalProductRecyclerAdapter, index: Int, view: View,
      additionalProductRepresentation: AdditionalProductRepresentation) {
      adapter.removeAdditionalProduct(index)
      shoppingListPreview.value!!.additionalProductList.remove(additionalProductRepresentation.data)
    }
  }

  val productDiscrepancyRecyclerAdapterCallback = object : ProductDiscrepancyRecyclerAdapter.Callback {
    override fun onEditButtonClicked(adapter: ProductDiscrepancyRecyclerAdapter, index: Int, view: View,
      productDiscrepancyRepresentation: ProductDiscrepancyRepresentation) {
      dialogEditShoppingProduct = productDiscrepancyRepresentation.data
      shoppingProductDialog.postValue(
        ShoppingProductDialog(R.string.change_cart_amount_for_product, object : ShoppingProductDialog.Callback {
          override fun onResult(shoppingProduct: ShoppingProduct) {
            adapter.replaceProductDiscrepancy(index, ProductDiscrepancyRepresentation(shoppingProduct))
            val shoppingList = mutableShoppingListPreview.value!!
            shoppingList.productDiscrepancyList.first { it == shoppingProduct }.cartAmount =
              shoppingProduct.cartAmount
            mutableShoppingListPreview.postValue(shoppingList)
          }
        }))
    }

    override fun onRemoveButtonClicked(adapter: ProductDiscrepancyRecyclerAdapter, index: Int, view: View,
      productDiscrepancyRepresentation: ProductDiscrepancyRepresentation) {
      adapter.removeProductDiscrepancy(index)
      shoppingListPreview.value!!.productDiscrepancyList.remove(productDiscrepancyRepresentation.data)
    }
  }

  suspend fun getProductForName(productName: String) = productRepository.getProductForName(productName)
}