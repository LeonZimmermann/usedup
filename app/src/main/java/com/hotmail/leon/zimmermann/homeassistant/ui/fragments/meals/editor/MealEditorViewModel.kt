package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.meals.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.*
import com.hotmail.leon.zimmermann.homeassistant.ui.components.consumption.ConsumptionElement
import kotlinx.coroutines.launch
import java.io.File

class MealEditorViewModel(application: Application) : AndroidViewModel(application) {
    private val database = Firebase.firestore
    var products: List<Pair<String, Product>> = ProductRepository.products
    val measures: MutableList<Pair<String, Measure>> = MeasureRepository.measures

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

    fun setMealId(mealId: String) {
        this.mealId = mealId
        database.collection(Meal.COLLECTION_NAME).document(mealId).get().addOnSuccessListener { result ->
            result.toObject<Meal>()?.apply {
                backgroundUrl?.let { photoFile = File(it) }
                nameString.value = name
                durationString.value = duration.toString()
                ingredients?.let {
                    consumptionElementList.value = it.map { ingredient ->
                        val product = products.first { it.first == ingredient.product!!.id }.second
                        val measure = MeasureRepository.getMeasureForId(ingredient.measure!!.id)
                        ConsumptionElement(
                            product,
                            Value(ingredient.value!!, measure)
                        )
                    }.toMutableList()
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
                this.value += consumptionElement.value
            }
                ?: list.add(consumptionElement)
        }
        this.consumptionElementList.value = consumptionElementList
    }

    fun addNewMealToDatabase() {
        // TODO Check if name is null and consumptionList empty (Validation)
        viewModelScope.launch {
            database.collection(Meal.COLLECTION_NAME)
                .add(
                    Meal(name!!, duration, description, instructions, photoFile.toString(),
                        consumptionElementList.value!!.map { element ->
                            MealIngredient(
                                database.collection(Product.COLLECTION_NAME)
                                    .document(products.first { it.second.name == element.product.name }.first),
                                database.collection(Measure.COLLECTION_NAME)
                                    .document(measures.first { it.second.name == element.value.measure.name }.first),
                                element.value.double
                            )
                        })
                )
        }
    }
}
