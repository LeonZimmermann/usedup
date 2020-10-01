package com.hotmail.leon.zimmermann.homeassistant.app.consumption

import android.app.Application
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.TemplateRepository

class ConsumptionViewModel(application: Application) : AndroidViewModel(application), AdapterView.OnItemClickListener {
  private val productList: MutableList<Product> = ProductRepository.products
  private val templateList: MutableList<Template> = TemplateRepository.templates
  private val mealList: MutableList<Meal> = MealRepository.meals
  private val measures: MutableList<Measure> = MeasureRepository.measures

  val nameList: MutableLiveData<List<String>> = MutableLiveData()
  val nameText: MutableLiveData<String> = MutableLiveData()

  val measureList: MutableLiveData<List<String>> = MutableLiveData()
  val measureText: MutableLiveData<String> = MutableLiveData()
  val measureInputType: MutableLiveData<Int> = MutableLiveData(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE)

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