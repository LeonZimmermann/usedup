package de.usedup.android.consumption

import android.text.InputType
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.datamodel.api.exceptions.InvalidInputException
import de.usedup.android.datamodel.api.objects.*
import de.usedup.android.datamodel.api.repositories.MealRepository
import de.usedup.android.datamodel.api.repositories.MeasureRepository
import de.usedup.android.datamodel.api.repositories.TemplateRepository
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ConsumptionViewModel @Inject constructor(
  private val mealRepository: MealRepository,
  private val measureRepository: MeasureRepository,
  private val templateRepository: TemplateRepository,
  private val productRepository: ProductRepository
) : ViewModel(), AdapterView.OnItemClickListener {

  private val productNameList: LiveData<List<String>> =
    Transformations.map(productRepository.getAllProductsLiveData(viewModelScope)) { it.map { product -> product.name } }
  private val templateNameList: LiveData<List<String>> =
    Transformations.map(templateRepository.getAllTemplates(viewModelScope)) { it.map { template -> template.name } }
  private val mealNameList: LiveData<List<String>> =
    Transformations.map(mealRepository.getAllMeals(viewModelScope)) { it.map { meal -> meal.name } }
  private val measures: MutableList<Measure> = measureRepository.measures

  private val consumptionCalculator = ConsumptionCalculator(measureRepository)

  val mappedNameList: MutableLiveData<LiveData<List<String>>> = MutableLiveData()
  val nameText: MutableLiveData<String> = MutableLiveData()
  val nameHint: MutableLiveData<String> = MutableLiveData()

  val quantityText: MutableLiveData<String> = MutableLiveData()
  val quantityInputFieldFocus: MutableLiveData<Boolean> = MutableLiveData()

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
    nameHint.postValue(mode.hint)
    if (nameList.value == null) this.nameText.postValue("")
    else this.nameText.postValue(if (nameList.value!!.size == 1) nameList.value!![0] else "")
  }

  fun consume() = viewModelScope.launch(Dispatchers.Default) {
    try {
      when (mode.value) {
        Mode.PRODUCT -> consumeProduct().join()
        Mode.TEMPLATE -> consumeTemplate().join()
        Mode.MEAL -> consumeMeal().join()
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

  private suspend fun consumeProduct() = viewModelScope.launch(Dispatchers.IO) {
    try {
      if (nameText.value.isNullOrBlank()) throw InvalidInputException("Please insert a name")
      if (quantityText.value.isNullOrBlank()) throw InvalidInputException("Please insert a quantity")
      val product =
        nameText.value?.let { productRepository.getProductForName(it.trim()) } ?: throw InvalidInputException(
          "Could not find product")
      val quantity =
        quantityText.value?.toDoubleOrNull() ?: throw InvalidInputException("Please insert a valid quantity")
      val measure =
        measures.find { it.name == measureText.value } ?: throw InvalidInputException("Could not find measure")
      val updatedQuantity = consumptionCalculator.calculateUpdatedQuantity(product, MeasureValue(quantity, measure))
      productRepository.changeQuantity(product, updatedQuantity)
    } catch (e: NotEnoughException) {
      errorMessage.postValue(e.message)
    } catch (e: InvalidInputException) {
      errorMessage.postValue(e.message)
    }
  }

  private suspend fun consumeTemplate() = viewModelScope.launch(Dispatchers.IO) {
    try {
      if (nameText.value.isNullOrBlank()) throw InvalidInputException("The template name is invalid")
      val template = nameText.value?.let { templateRepository.getTemplateForName(it) }!!
      val data = template.components.map { element ->
        val product = productRepository.getProductForId(element.productId) ?: throw InvalidInputException(
          "The template has invalid products and could not be consumed")
        val quantity = element.value
        val measure = measures.find { it.id == element.measureId } ?: throw RuntimeException()
        Pair(product, consumptionCalculator.calculateUpdatedQuantity(product, MeasureValue(quantity, measure)))
      }
      productRepository.changeQuantity(data)
    } catch (e: NotEnoughException) {
      errorMessage.postValue(e.message)
    } catch (e: InvalidInputException) {
      errorMessage.postValue(e.message)
    }
  }

  private suspend fun consumeMeal() = viewModelScope.launch(Dispatchers.IO) {
    try {
      if (nameText.value.isNullOrBlank()) throw InvalidInputException("The meal name is invalid")
      val meal = nameText.value?.let { mealRepository.getMealForName(it) }!!
      val data = meal.ingredients.map { element ->
        val product = productRepository.getProductForId(element.productId) ?: throw InvalidInputException(
          "The meal has invalid products and could not be consumed")
        val quantity = element.value
        val measure = measures.find { it.id == element.measureId } ?: throw RuntimeException()
        Pair(product, consumptionCalculator.calculateUpdatedQuantity(product, MeasureValue(quantity, measure)))
      }
      productRepository.changeQuantity(data)
    } catch (e: NotEnoughException) {
      errorMessage.postValue(e.message)
    } catch (e: InvalidInputException) {
      errorMessage.postValue(e.message)
    }
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
        val product = productRepository.getProductForName(productNameList.value!![position])
        if (product != null) {
          val productMeasure = measureRepository.getMeasureForId(product.measureId)
          if (productMeasure != null) {
            val measureList = measures.filter { it.type == productMeasure.type }.map { it.name }
            this@ConsumptionViewModel.measureNameList.value = measureList
            measureText.value = if (measureList.size > 1) "" else productMeasure.name
            measureInputType.value = if (measureList.size > 1) InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE else 0
            quantityInputFieldFocus.postValue(true)
          }
        }
      }
    }
  }

  enum class Mode(internal val hint: String) {
    PRODUCT("Enter the products name"),
    TEMPLATE("Enter the templates name"),
    MEAL("Enter the meals name")
  }
}