package de.usedup.android.management.meals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.components.consumption.ConsumptionElement
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.MealIngredient
import de.usedup.android.datamodel.api.objects.MeasureValue
import de.usedup.android.datamodel.api.objects.Product
import de.usedup.android.datamodel.api.repositories.MealRepository
import de.usedup.android.datamodel.api.repositories.MeasureRepository
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MealEditorViewModel @Inject constructor(
  private val productRepository: ProductRepository,
  private val measureRepository: MeasureRepository,
  private val mealRepository: MealRepository
) : ViewModel() {

  val products: LiveData<Set<Product>> = productRepository.getAllProductsLiveData(viewModelScope)

  val nameString = MutableLiveData<String>()
  val durationString = MutableLiveData<String>()
  val descriptionString = MutableLiveData<String?>()
  val instructionsString = MutableLiveData<String?>()
  val consumptionElementList: MutableLiveData<MutableList<ConsumptionElement>> = MutableLiveData(mutableListOf())
  var photoFile: File? = null

  private val name: String?
    get() = nameString.value
  private val duration: Int?
    get() = durationString.value?.toInt()
  private val description: String?
    get() = descriptionString.value
  private val instructions: String?
    get() = instructionsString.value

  val errorMessage: MutableLiveData<String> = MutableLiveData()

  var mealId: Id? = null
    private set

  fun setMealId(mealId: Id) = viewModelScope.launch(Dispatchers.IO) {
    this@MealEditorViewModel.mealId = mealId
    mealRepository.getMealForId(mealId)?.apply {
      backgroundUrl?.let { photoFile = File(it) }
      nameString.postValue(name)
      durationString.postValue(duration.toString())
      ingredients.let {
        consumptionElementList.postValue(it.mapNotNull { ingredient ->
          val product = productRepository.getProductForId(ingredient.productId)
          val measure = measureRepository.getMeasureForId(ingredient.measureId)
          if (product != null && measure != null) {
            ConsumptionElement(product, MeasureValue(ingredient.value, measure))
          } else {
            null
          }
        }.toMutableList())
      }
      descriptionString.postValue(description)
      instructionsString.postValue(instructions)
    }
  }

  fun addConsumptionElement(consumptionElement: ConsumptionElement) {
    consumptionElementList.value?.let { list ->
      val foundConsumptionElement = list.firstOrNull { it.product == consumptionElement.product }
      if (foundConsumptionElement != null) foundConsumptionElement.valueValue += consumptionElement.valueValue
      else list.add(consumptionElement)
      this.consumptionElementList.postValue(list)
    }
  }

  fun addNewMealToDatabase() = viewModelScope.launch(Dispatchers.IO) {
    when {
      name.isNullOrBlank() -> errorMessage.postValue("Insert a name")
      duration == null -> errorMessage.postValue("Insert a duration")
      requireNotNull(consumptionElementList.value).isEmpty() -> errorMessage.postValue("Insert consumption elements")
      else -> mealRepository.addMeal(requireNotNull(name), requireNotNull(duration), description, instructions,
        photoFile?.toString(),
        consumptionElementList.value!!.map { element ->
          MealIngredient(element.product.id, element.valueValue.measure.id, element.valueValue.double)
        })
    }
  }
}
