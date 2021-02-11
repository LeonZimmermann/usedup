package de.usedup.android.shopping.preview

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import androidx.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.R
import de.usedup.android.datamodel.api.repositories.MealRepository
import de.usedup.android.datamodel.api.repositories.MeasureRepository
import de.usedup.android.datamodel.api.repositories.PlannerRepository
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import de.usedup.android.shopping.data.ShoppingProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListPreviewViewModel @Inject constructor(
  private val productRepository: ProductRepository,
  mealRepository: MealRepository,
  plannerRepository: PlannerRepository,
  measureRepository: MeasureRepository) : ViewModel(), Parcelable {

  private val shoppingListPreviewGenerator =
    ShoppingListPreviewGenerator(productRepository, mealRepository, plannerRepository)
  private val mapper = ShoppingListPreviewToShoppingListMapper(productRepository, measureRepository)

  private val mutableShoppingListPreview = MutableLiveData<ShoppingListPreview>()
  val shoppingListPreview: LiveData<ShoppingListPreview> = Transformations.map(mutableShoppingListPreview) { it }

  val addAdditionalProductShoppingProduct: MutableLiveData<ShoppingProduct?> = MutableLiveData(null)
  val editAdditionalProductShoppingProduct: MutableLiveData<ShoppingProduct?> = MutableLiveData(null)

  // Dialog
  val productNames: LiveData<List<String>> =
    Transformations.map(productRepository.products) { products -> products.map { it.name }.toList() }
  var shoppingProductDialog: MutableLiveData<ShoppingProductDialog?> = MutableLiveData(null)
  var dialogEditShoppingProduct: ShoppingProduct? = null

  fun initShoppingListPreview() {
    viewModelScope.launch(Dispatchers.IO) {
      mutableShoppingListPreview.postValue(shoppingListPreviewGenerator.generateShoppingListPreview())
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

  constructor(parcel: Parcel) : this(
    TODO("productRepository"),
    TODO("mealRepository"),
    TODO("plannerRepository"),
    TODO("measureRepository")) {

  }

  suspend fun getProductForName(productName: String) = productRepository.getProductForName(productName)
  override fun writeToParcel(parcel: Parcel, flags: Int) {

  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<ShoppingListPreviewViewModel> {
    override fun createFromParcel(parcel: Parcel): ShoppingListPreviewViewModel {
      return ShoppingListPreviewViewModel(parcel)
    }

    override fun newArray(size: Int): Array<ShoppingListPreviewViewModel?> {
      return arrayOfNulls(size)
    }
  }
}