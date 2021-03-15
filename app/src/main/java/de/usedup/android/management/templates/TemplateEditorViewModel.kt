package de.usedup.android.management.templates

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import de.usedup.android.components.consumption.ConsumptionElement
import de.usedup.android.datamodel.api.exceptions.InvalidInputException
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.MeasureValue
import de.usedup.android.datamodel.api.objects.Product
import de.usedup.android.datamodel.api.objects.TemplateComponent
import de.usedup.android.datamodel.api.repositories.MeasureRepository
import de.usedup.android.datamodel.api.repositories.TemplateRepository
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


class TemplateEditorViewModel @ViewModelInject constructor(
  private val productRepository: ProductRepository,
  private val measureRepository: MeasureRepository,
  private val templateRepository: TemplateRepository
) : ViewModel() {

  var products: MutableLiveData<MutableList<Product>> = productRepository.products

  var nameString = MutableLiveData<String>()
  val consumptionElementList: MutableLiveData<MutableList<ConsumptionElement>> = MutableLiveData(mutableListOf())
  val buttonTopMargin: MutableLiveData<Int> = MutableLiveData()
  val actionButtonText: MutableLiveData<String> = MutableLiveData("Add Template")
  val errorMessage: MutableLiveData<String> = MutableLiveData()

  private var templateId: Id? = null
    private set

  fun setTemplateId(templateId: Id) = viewModelScope.launch(Dispatchers.IO) {
    this@TemplateEditorViewModel.templateId = templateId
    actionButtonText.postValue("Update Template")
    templateRepository.getTemplateForId(templateId)?.apply {
      nameString.postValue(name)
      components.let {
        consumptionElementList.postValue(it.mapNotNull { element ->
          val product = productRepository.getProductForId(element.productId)
          val measure = measureRepository.getMeasureForId(element.measureId)
          if (product != null && measure != null) {
            ConsumptionElement(product, MeasureValue(element.value, measure))
          } else {
            null
          }
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
      if (templateId == null) templateRepository.addTemplate(name, consumptionElementList)
      else templateRepository.updateTemplate(templateId!!, name, consumptionElementList)
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