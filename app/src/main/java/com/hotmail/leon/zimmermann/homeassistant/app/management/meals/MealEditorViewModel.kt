package com.hotmail.leon.zimmermann.homeassistant.app.management.meals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.components.consumption.ConsumptionElement
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseMeal
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class MealEditorViewModel(application: Application) : AndroidViewModel(application) {
  private val database = Firebase.firestore
  var products: MutableLiveData<MutableList<Product>> = ProductRepository.products

  var nameString = MutableLiveData<String>()
  var durationString = MutableLiveData<String>()
  var descriptionString = MutableLiveData<String>()
  var instructionsString = MutableLiveData<String>()
  val consumptionElementList: MutableLiveData<MutableList<ConsumptionElement>> by lazy {
    MutableLiveData<MutableList<ConsumptionElement>>().apply {
      value = mutableListOf()
    }
  }
  var photoFile: File? = null

  private val name: String?
    get() = nameString.value
  private val duration: Int?
    get() = durationString.value?.toInt()
  private val description: String?
    get() = descriptionString.value
  private val instructions: String?
    get() = instructionsString.value

  var mealId: String? = null
    private set

  fun setMealId(mealId: String) = viewModelScope.launch(Dispatchers.IO) {
    this@MealEditorViewModel.mealId = mealId
    val task = database.collection(FirebaseMeal.COLLECTION_NAME).document(mealId).get()
    Tasks.await(task)
    if (task.exception != null) throw task.exception!!
    else {
      task.result!!.toObject<Meal>()?.apply {
        backgroundUrl?.let { photoFile = File(it) }
        nameString.postValue(name)
        durationString.postValue(duration.toString())
        ingredients.let {
          consumptionElementList.postValue(it.map { ingredient ->
            val product = ProductRepository.getProductForId(ingredient.productId)
            val measure = MeasureRepository.getMeasureForId(ingredient.measureId)
            ConsumptionElement(product, MeasureValue(ingredient.value, measure))
          }.toMutableList())
        }
        descriptionString.value = description
        instructionsString.value = instructions
      } ?: throw RuntimeException("Couln't convert meal") // TODO Should not be a RuntimeException
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
    MealRepository.addMeal(name!!, duration!!, description, instructions, photoFile.toString(),
      consumptionElementList.value!!.map { element ->
        MealIngredient(
          element.product.id,
          element.valueValue.measure.id,
          element.valueValue.double
        )
      })
  }
}
