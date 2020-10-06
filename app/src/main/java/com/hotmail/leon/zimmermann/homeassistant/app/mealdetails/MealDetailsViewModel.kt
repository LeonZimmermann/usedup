package com.hotmail.leon.zimmermann.homeassistant.app.mealdetails

import android.app.Application
import android.text.Html
import android.text.Spanned
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.components.consumption.ConsumptionElement
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.MeasureValue
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MealDetailsViewModel(application: Application) : AndroidViewModel(application) {
  var nameString = MutableLiveData<String>()
  var durationString = MutableLiveData<String>()
  var descriptionString = MutableLiveData<String>()
  var instructionsString = MutableLiveData<String>()
  val ingredientsString: MutableLiveData<Spanned> = MutableLiveData()

  var mealId: String? = null
    private set

  fun setMealId(mealId: String) = viewModelScope.launch(Dispatchers.IO) {
    this@MealDetailsViewModel.mealId = mealId
    MealRepository.getMealForId(mealId).apply {
      nameString.postValue(name)
      durationString.postValue(duration.toString())
      ingredientsString.postValue(createIngredientsString(ingredients.map { ingredient ->
        val product = ProductRepository.getProductForId(ingredient.productId)
        val measure = MeasureRepository.getMeasureForId(ingredient.measureId)
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
