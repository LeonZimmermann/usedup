package de.usedup.android.management.meals

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.usedup.android.components.consumption.ConsumptionElement
import de.usedup.android.datamodel.api.exceptions.InvalidInputException
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

class MealEditorViewModel @ViewModelInject constructor(
  private val productRepository: ProductRepository,
  private val measureRepository: MeasureRepository,
  private val mealRepository: MealRepository
) : ViewModel() {

  var products: MutableLiveData<MutableList<Product>> = productRepository.products

  var nameString = MutableLiveData<String>()
  var durationString = MutableLiveData<String>()
  var descriptionString = MutableLiveData<String>()
  var instructionsString = MutableLiveData<String>()
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

  var mealId: Id? = null
    private set

  fun setMealId(mealId: Id) = viewModelScope.launch(Dispatchers.IO) {
    this@MealEditorViewModel.mealId = mealId
    mealRepository.getMealForId(mealId).apply {
      backgroundUrl?.let { photoFile = File(it) }
      nameString.postValue(name)
      durationString.postValue(duration.toString())
      ingredients.let {
        consumptionElementList.postValue(it.map { ingredient ->
          val product = productRepository.getProductForId(ingredient.productId)
          val measure = measureRepository.getMeasureForId(ingredient.measureId)
          ConsumptionElement(product, MeasureValue(ingredient.value, measure))
        }.toMutableList())
      }
      descriptionString.postValue(description)
      instructionsString.postValue(instructions)
    }
  }

  fun addConsumptionElement(consumptionElement: ConsumptionElement) {
    val consumptionElementList = consumptionElementList.value
    consumptionElementList?.let { list ->
      list.firstOrNull { it.product == consumptionElement.product }?.apply {
        this.valueValue += consumptionElement.valueValue
      }
        ?: list.add(consumptionElement)
    }
    this.consumptionElementList.value = consumptionElementList
  }

  fun addNewMealToDatabase() = viewModelScope.launch(Dispatchers.IO) {
    // TODO Check if name is null and consumptionList empty (Validation)
    when {
      name.isNullOrBlank() -> throw InvalidInputException("Insert a name")
      duration == null -> throw InvalidInputException("Insert a duration")
      consumptionElementList.value!!.isEmpty() -> throw InvalidInputException("Insert consumption elements")
      else -> mealRepository.addMeal(name!!, duration!!, description, instructions, photoFile?.toString(),
        consumptionElementList.value!!.map { element ->
          MealIngredient(element.product.id, element.valueValue.measure.id, element.valueValue.double)
        })
    }

  }
}
