package de.usedup.android.mealdetails

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.components.consumption.ConsumptionElement
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.MeasureValue
import de.usedup.android.datamodel.api.repositories.ImageRepository
import de.usedup.android.datamodel.api.repositories.MealRepository
import de.usedup.android.datamodel.api.repositories.MeasureRepository
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import de.usedup.android.utils.toDurationString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealDetailsViewModel @Inject constructor(
  private val productRepository: ProductRepository,
  private val measureRepository: MeasureRepository,
  private val mealRepository: MealRepository,
  private val imageRepository: ImageRepository,
) : ViewModel() {

  val nameString = MutableLiveData<String>()
  val durationString = MutableLiveData<String>()
  val descriptionString = MutableLiveData<String>()
  val instructionsString = MutableLiveData<String>()
  val ingredientsString: MutableLiveData<String> = MutableLiveData()
  val image: MutableLiveData<Bitmap> = MutableLiveData()

  val errorMessage = MutableLiveData<String?>()
  val navigateUp = MutableLiveData(false)

  private var mealId: Id? = null
    private set

  fun setMealId(mealId: Id) = viewModelScope.launch(Dispatchers.IO) {
    this@MealDetailsViewModel.mealId = mealId
    mealRepository.getMealForId(mealId)?.apply {
      imageName?.let { imageName ->
        viewModelScope.launch(Dispatchers.IO) {
          imageRepository.getImage(imageName).subscribe {
            image.postValue(it)
          }
        }
      }
      nameString.postValue(name)
      durationString.postValue(duration.toDurationString())
      ingredientsString.postValue(createIngredientsString(ingredients.mapNotNull { ingredient ->
        val product = productRepository.getProductForId(ingredient.productId)
        val measure = measureRepository.getMeasureForId(ingredient.measureId)
        if (product != null && measure != null) {
          ConsumptionElement(product, MeasureValue(ingredient.value, measure))
        } else {
          null
        }
      }))
      descriptionString.postValue(description ?: "There is no description for this meal")
      instructionsString.postValue(instructions ?: "There are no instructions for this meal")
    } ?: run {
      errorMessage.postValue("An error occured loading the meal")
      navigateUp.postValue(true)
    }
  }

  private fun createIngredientsString(consumptionElements: List<ConsumptionElement>): String {
    return if (consumptionElements.isEmpty()) {
      "There is no consumption list for this meal"
    } else {
      consumptionElements.joinToString(separator = "\n") {
        "${it.valueValue.double} ${it.valueValue.measure.name} of ${it.product.name}"
      }
    }
  }
}
