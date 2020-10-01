package com.hotmail.leon.zimmermann.homeassistant.app.consumption

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.InvalidInputException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.NotEnoughException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.TemplateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConsumptionViewModel(application: Application) : AndroidViewModel(application), AdapterView.OnItemClickListener {
  private val handler = Handler(Looper.getMainLooper())

  private val productList: MutableList<Product> = ProductRepository.products
  private val templateList: MutableList<Template> = TemplateRepository.templates
  private val mealList: MutableList<Meal> = MealRepository.meals
  private val measures: MutableList<Measure> = MeasureRepository.measures

  private val consumptionCalculator = ConsumptionCalculator()
  private val consumptionDatabaseProcessor = ConsumptionDatabaseProcessor()
  private val consumptionInMemoryProcessor = ConsumptionInMemoryProcessor()

  val nameList: MutableLiveData<List<String>> = MutableLiveData()
  val nameText: MutableLiveData<String> = MutableLiveData()

  val quantityText: MutableLiveData<String> = MutableLiveData()

  val measureList: MutableLiveData<List<String>> = MutableLiveData()
  val measureText: MutableLiveData<String> = MutableLiveData()
  val measureInputType: MutableLiveData<Int> = MutableLiveData(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE)

  val errorMessage: MutableLiveData<String> = MutableLiveData()

  val mode = MutableLiveData<Mode>()

  init {
    setMode(Mode.PRODUCT)
  }

  fun setMode(mode: Mode) {
    val nameList = when (mode) {
      Mode.PRODUCT -> productList.map { it.name }
      Mode.TEMPLATE -> templateList.map { it.name }
      Mode.MEAL -> mealList.map { it.name }
    }
    this.mode.value = mode
    this.nameList.value = nameList
    this.nameText.value = if (nameList.size == 1) nameList[0] else ""
  }

  fun consume() = viewModelScope.launch(Dispatchers.Default) {
    try {
      when (mode.value) {
        Mode.PRODUCT -> consumeProduct()
        Mode.TEMPLATE -> consumeTemplate()
        Mode.MEAL -> consumeMeal()
      }
      clearInputs()
    } catch (e: NotEnoughException) {
      handler.post { errorMessage.value = e.message }
    } catch (e: InvalidInputException) {
      handler.post { errorMessage.value = e.message }
    }
  }

  private fun consumeProduct() {
    val product =
      productList.find { it.name == nameText.value } ?: throw InvalidInputException("The product name is invalid")
    val quantity = quantityText.value?.toDouble() ?: throw InvalidInputException("The quantity is invalid")
    val measure =
      measures.find { it.name == measureText.value } ?: throw InvalidInputException("The measure is invalid")
    val updatedQuantity = consumptionCalculator.calculateUpdatedQuantity(product, MeasureValue(quantity, measure))
    consumptionDatabaseProcessor.updateSingleProductQuantity(product, updatedQuantity)
    consumptionInMemoryProcessor.updateSingleProductQuantity(product, updatedQuantity)
  }

  private fun consumeTemplate() {
    val template =
      templateList.find { it.name == nameText.value } ?: throw InvalidInputException("The template name is invalid")
    val data = template.components.map { element ->
      val product = productList.find { it.id == element.productId } ?: throw RuntimeException()
      val quantity = element.value
      val measure = measures.find { it.name == measureText.value } ?: throw RuntimeException()
      Pair(product, consumptionCalculator.calculateUpdatedQuantity(product, MeasureValue(quantity, measure)))
    }
    consumptionDatabaseProcessor.updateMultipleProductQuantities(data)
    consumptionInMemoryProcessor.updateMultipleProductQuantities(data)
  }

  private fun consumeMeal() {
    val meal = mealList.find { it.name == nameText.value } ?: throw InvalidInputException("The meal name is invalid")
    val data = meal.ingredients.map { element ->
      val product = productList.find { it.id == element.productId } ?: throw RuntimeException()
      val quantity = element.value
      val measure = measures.find { it.name == measureText.value } ?: throw RuntimeException()
      Pair(product, consumptionCalculator.calculateUpdatedQuantity(product, MeasureValue(quantity, measure)))
    }
    consumptionDatabaseProcessor.updateMultipleProductQuantities(data)
    consumptionInMemoryProcessor.updateMultipleProductQuantities(data)
  }

  private fun clearInputs() {
    nameText.value = ""
    quantityText.value = ""
    measureText.value = ""
    measureList.value = listOf()
  }

  override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    if (mode.value == Mode.PRODUCT) {
      val productMeasure = MeasureRepository.getMeasureForId(productList[position].measureId)
      val measureList = measures.filter { it.type == productMeasure.type }.map { it.name }
      this.measureList.value = measureList
      measureText.value = if (measureList.size > 1) "" else productMeasure.name
      measureInputType.value = if (measureList.size > 1) InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE else 0
    }
  }

  enum class Mode {
    PRODUCT, TEMPLATE, MEAL
  }
}