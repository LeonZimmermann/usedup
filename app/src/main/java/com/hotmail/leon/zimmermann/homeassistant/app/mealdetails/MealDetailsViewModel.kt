package com.hotmail.leon.zimmermann.homeassistant.app.mealdetails

import android.text.Html
import android.text.Spanned
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.components.consumption.ConsumptionElement
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.MeasureValue
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MealDetailsViewModel @ViewModelInject constructor(
  private val productRepository: ProductRepository,
  private val measureRepository: MeasureRepository,
  private val mealRepository: MealRepository
) : ViewModel() {

  var nameString = MutableLiveData<String>()
  var durationString = MutableLiveData<String>()
  var descriptionString = MutableLiveData<String>()
  var instructionsString = MutableLiveData<String>()
  val ingredientsString: MutableLiveData<Spanned> = MutableLiveData()

  var mealId: Id? = null
    private set

  fun setMealId(mealId: Id) = viewModelScope.launch(Dispatchers.IO) {
    this@MealDetailsViewModel.mealId = mealId
    mealRepository.getMealForId(mealId).apply {
      nameString.postValue(name)
      durationString.postValue(duration.toString())
      ingredientsString.postValue(createIngredientsString(ingredients.map { ingredient ->
        val product = productRepository.getProductForId(ingredient.productId)
        val measure = measureRepository.getMeasureForId(ingredient.measureId)
        ConsumptionElement(product, MeasureValue(ingredient.value, measure))
      }))
      descriptionString.postValue(description)
      instructionsString.postValue(instructions)
    }
  }

  private fun createIngredientsString(consumptionElements: List<ConsumptionElement>): Spanned {
    val htmlText = consumptionElements.joinToString(separator = "", prefix = "<ul>", postfix = "</ul>") {
      "<li>${it.valueValue.double} ${it.valueValue.measure.name} of ${it.product.name}</li>"
    }
    return Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT)
  }
}
