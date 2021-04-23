package de.usedup.android.management.meals

import android.graphics.Bitmap
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
import de.usedup.android.datamodel.api.repositories.*
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MealEditorViewModel @Inject constructor(
  private val productRepository: ProductRepository,
  private val measureRepository: MeasureRepository,
  private val mealRepository: MealRepository,
  private val imageRepository: ImageRepository,
) : ViewModel() {

  val products: LiveData<Set<Product>> = productRepository.getAllProductsLiveData(viewModelScope)

  val nameString = MutableLiveData<String>()
  val durationString = MutableLiveData<String>()
  val descriptionString = MutableLiveData<String?>()
  val instructionsString = MutableLiveData<String?>()
  val consumptionElementList: MutableLiveData<MutableList<ConsumptionElement>> = MutableLiveData(mutableListOf())
  val image: MutableLiveData<Bitmap> = MutableLiveData()
  var oldImageName: String? = null
  var newImageName: String? = null

  private val name: String?
    get() = nameString.value
  private val duration: Int?
    get() = durationString.value?.toInt()
  private val description: String?
    get() = descriptionString.value
  private val instructions: String?
    get() = instructionsString.value

  val addIngredientsClicked: MutableLiveData<Boolean> = MutableLiveData()
  val openCamera: MutableLiveData<Boolean> = MutableLiveData()
  val navigateUp: MutableLiveData<Boolean> = MutableLiveData()
  val errorMessage: MutableLiveData<String> = MutableLiveData()

  var mealId: Id? = null
    private set

  fun setMealId(mealId: Id) = viewModelScope.launch(Dispatchers.IO) {
    this@MealEditorViewModel.mealId = mealId
    mealRepository.getMealForId(mealId)?.apply {
      imageName?.let { imageName ->
        oldImageName = imageName
        viewModelScope.launch(Dispatchers.IO) {
          imageRepository.getImage(imageName)
            .doOnError { errorMessage.postValue("Could not load image") }
            .subscribe { image.postValue(it) }
        }
      }
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

  fun setImageFromBitmap(bitmap: Bitmap) {
    newImageName = UUID.randomUUID().toString()
    image.postValue(bitmap)
  }

  fun addConsumptionElement(consumptionElement: ConsumptionElement) {
    consumptionElementList.value?.let { list ->
      val foundConsumptionElement = list.firstOrNull { it.product == consumptionElement.product }
      if (foundConsumptionElement != null) foundConsumptionElement.valueValue += consumptionElement.valueValue
      else list.add(consumptionElement)
      this.consumptionElementList.postValue(list)
    }
  }

  fun onImageViewClicked() {
    openCamera.postValue(true)
  }

  fun onAddIngredientsClicked() {
    addIngredientsClicked.postValue(true)
  }

  fun onSaveDinnerButtonClicked() {
    viewModelScope.launch(Dispatchers.IO) {
      when {
        name.isNullOrBlank() -> errorMessage.postValue("Insert a name")
        duration == null -> errorMessage.postValue("Insert a duration")
        requireNotNull(consumptionElementList.value).isEmpty() -> errorMessage.postValue("Insert consumption elements")
        else -> {
          val consumptions = requireNotNull(consumptionElementList.value).map { element ->
            MealIngredient(element.product.id, element.valueValue.measure.id, element.valueValue.double)
          }
          if (mealId == null) {
            mealRepository.addMeal(requireNotNull(name), requireNotNull(duration), description, instructions,
              newImageName, consumptions)
            newImageName?.let { imageRepository.createImage(it, requireNotNull(image.value)) }
          } else {
            if (newImageName == null) {
              mealRepository.updateMeal(requireNotNull(mealId), requireNotNull(name), requireNotNull(duration),
                description, instructions, oldImageName, consumptions)
            } else {
              if (oldImageName != null) {
                imageRepository.deleteImage(requireNotNull(oldImageName))
              }
              imageRepository.createImage(requireNotNull(newImageName), requireNotNull(image.value))
              mealRepository.updateMeal(requireNotNull(mealId), requireNotNull(name), requireNotNull(duration),
                description, instructions, newImageName, consumptions)
            }
          }
          navigateUp.postValue(true)
        }
      }
    }
  }
}
