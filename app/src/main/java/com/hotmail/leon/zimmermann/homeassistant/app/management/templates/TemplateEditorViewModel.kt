package com.hotmail.leon.zimmermann.homeassistant.app.management.templates

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.hotmail.leon.zimmermann.homeassistant.components.R.dimen.lMargin
import com.hotmail.leon.zimmermann.homeassistant.components.R.dimen.sMargin
import com.hotmail.leon.zimmermann.homeassistant.components.consumption.ConsumptionElement
import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.InvalidInputException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.MeasureValue
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.TemplateComponent
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.TemplateRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


class TemplateEditorViewModel : ViewModel() {
  var products: MutableLiveData<MutableList<Product>> = ProductRepository.products

  var nameString = MutableLiveData<String>()
  val consumptionElementList: MutableLiveData<MutableList<ConsumptionElement>> = MutableLiveData(mutableListOf())
  val buttonTopMargin: MutableLiveData<Int> = MutableLiveData()
  val actionButtonText: MutableLiveData<String> = MutableLiveData("Add Template")
  val errorMessage: MutableLiveData<String> = MutableLiveData()

  var templateId: String? = null
    private set

  fun setTemplateId(templateId: String) = viewModelScope.launch(Dispatchers.IO) {
    this@TemplateEditorViewModel.templateId = templateId
    actionButtonText.postValue("Update Template")
    TemplateRepository.getTemplateForId(templateId).apply {
      nameString.postValue(name)
      components.let {
        consumptionElementList.postValue(it.map { element ->
          val product = ProductRepository.getProductForId(element.productId)
          val measure = MeasureRepository.getMeasureForId(element.measureId)
          ConsumptionElement(product, MeasureValue(element.value, measure))
        }.toMutableList())
      }
    }
  }

  fun onSaveTemplateButtonClicked(view: View) = viewModelScope.launch(Dispatchers.IO) {
    try {
      val name = nameString.value ?: throw InvalidInputException("Please insert a valid name")
      val consumptionElementList = this@TemplateEditorViewModel.consumptionElementList.value!!.map { element ->
        TemplateComponent(element.product.id, element.valueValue.measure.id, element.valueValue.double)
      }
      if (consumptionElementList.isNullOrEmpty()) throw InvalidInputException("You need to give at least one component")
      if (templateId == null) TemplateRepository.addTemplate(name, consumptionElementList)
      else TemplateRepository.updateTemplate(templateId!!, name, consumptionElementList)
      Navigation.findNavController(view).navigateUp()
    } catch (e: InvalidInputException) {
      errorMessage.postValue(e.message)
    } catch (e: IOException) {
      errorMessage.postValue("A database error occurred")
    } catch (e: NoSuchElementException) {
      throw RuntimeException()
    }
  }

  fun addConsumptionElement(consumptionElement: ConsumptionElement) {
    var consumptionElementList = this.consumptionElementList.value
    if (consumptionElementList == null) {
      consumptionElementList = mutableListOf()
      this.consumptionElementList.postValue(consumptionElementList)
    }
    consumptionElementList.firstOrNull { it.product == consumptionElement.product }?.apply {
      this.valueValue += consumptionElement.valueValue
    } ?: consumptionElementList.add(consumptionElement)
    this.consumptionElementList.postValue(consumptionElementList)
    //buttonTopMargin.postValue(if (consumptionElementList.isEmpty()) lMargin else sMargin)
  }

  fun onConsumptionElementRemoved(position: Int) {
    val consumptionElementList = this.consumptionElementList.value!!
    consumptionElementList.removeAt(position)
    this.consumptionElementList.postValue(consumptionElementList)
    //buttonTopMargin.postValue(if (consumptionElementList.isEmpty()) lMargin else sMargin)
  }

  companion object {
    @JvmStatic
    @BindingAdapter("android:layout_marginTop")
    fun setLayoutMarginTop(view: TextView, marginTop: Int) {
      if (view.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = view.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(p.leftMargin, marginTop, p.rightMargin, p.bottomMargin)
        view.requestLayout()
      }
    }
  }
}