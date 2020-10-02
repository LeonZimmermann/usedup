package com.hotmail.leon.zimmermann.homeassistant.app.consumption

import android.app.Application
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.InvalidInputException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.TemplateRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class ConsumptionViewModel(application: Application) : AndroidViewModel(application), AdapterView.OnItemClickListener {
  private val productNameList = Transformations.map(ProductRepository.products) { products -> products.map { it.name } }
  private val templateNameList =
    Transformations.map(TemplateRepository.templates) { templates -> templates.map { it.name } }
  private val mealNameList = Transformations.map(MealRepository.meals) { meals -> meals.map { it.name } }
  private val measures: MutableList<Measure> = MeasureRepository.measures

  private val consumptionCalculator = ConsumptionCalculator()

  val mappedNameList: MutableLiveData<LiveData<List<String>>> = MutableLiveData()
  val nameText: MutableLiveData<String> = MutableLiveData()

  val quantityText: MutableLiveData<String> = MutableLiveData()

  val measureNameList: MutableLiveData<List<String>> = MutableLiveData()
  val measureText: MutableLiveData<String> = MutableLiveData()
  val measureInputType: MutableLiveData<Int> = MutableLiveData(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE)

  val errorMessage: MutableLiveData<String> = MutableLiveData()

  val mode = MutableLiveData<Mode>()

  init {
    setMode(Mode.PRODUCT)
  }

  fun setMode(mode: Mode) {
    val nameList = when (mode) {
      Mode.PRODUCT -> productNameList
      Mode.TEMPLATE -> templateNameList
      Mode.MEAL -> mealNameList
    }
    this.mode.value = mode
    this.mappedNameList.value = nameList
    if (nameList.value == null) this.nameText.value = ""
    else this.nameText.value = if (nameList.value!!.size == 1) nameList.value!![0] else ""
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
      errorMessage.postValue(e.message)
    } catch (e: InvalidInputException) {
      errorMessage.postValue(e.message)
    } catch (e: IOException) {
      errorMessage.postValue("A database error occurred")
    }
  }

  private suspend fun consumeProduct() {
    val product = nameText.value?.let { ProductRepository.getProductForName(it) } ?: throw InvalidInputException(
      "The product name is invalid")
    val quantity = quantityText.value?.toDouble() ?: throw InvalidInputException("The quantity is invalid")
    val measure =
      measures.find { it.name == measureText.value } ?: throw InvalidInputException("The measure is invalid")
    val updatedQuantity = consumptionCalculator.calculateUpdatedQuantity(product, MeasureValue(quantity, measure))
    ProductRepository.changeQuantity(product, updatedQuantity)
  }

  private suspend fun consumeTemplate() {
    val template = nameText.value?.let { TemplateRepository.getTemplateForName(it) } ?: throw InvalidInputException(
      "The template name is invalid")
    val data = template.components.map { element ->
      val product = ProductRepository.getProductForId(element.productId)
      val quantity = element.value
      val measure = measures.find { it.name == measureText.value } ?: throw RuntimeException()
      Pair(product, consumptionCalculator.calculateUpdatedQuantity(product, MeasureValue(quantity, measure)))
    }
    ProductRepository.changeQuantity(data)
  }

  private suspend fun consumeMeal() {
    val meal = nameText.value?.let { MealRepository.getMealForName(it) } ?: throw InvalidInputException(
      "The meal name is invalid")
    val data = meal.ingredients.map { element ->
      val product = ProductRepository.getProductForId(element.productId)
      val quantity = element.value
      val measure = measures.find { it.name == measureText.value } ?: throw RuntimeException()
      Pair(product, consumptionCalculator.calculateUpdatedQuantity(product, MeasureValue(quantity, measure)))
    }
    ProductRepository.changeQuantity(data)
  }

  private fun clearInputs() {
    nameText.postValue("")
    quantityText.postValue("")
    measureText.postValue("")
    measureNameList.postValue(listOf())
  }

  override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    viewModelScope.launch {
      if (mode.value == Mode.PRODUCT) {
        val product = ProductRepository.getProductForName(productNameList.value!![position])
        val productMeasure = MeasureRepository.getMeasureForId(product.measureId)
        val measureList = measures.filter { it.type == productMeasure.type }.map { it.name }
        this@ConsumptionViewModel.measureNameList.value = measureList
        measureText.value = if (measureList.size > 1) "" else productMeasure.name
        measureInputType.value = if (measureList.size > 1) InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE else 0
      }
    }
  }

  enum class Mode {
    PRODUCT, TEMPLATE, MEAL
  }
}