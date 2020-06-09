package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.meals.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.*
import kotlinx.coroutines.launch
import java.io.File

class MealEditorViewModel(application: Application) : AndroidViewModel(application) {
    private val database = Firebase.firestore
    val products: MutableLiveData<List<Pair<String, Product>>> = MutableLiveData(listOf())
    val measures: MutableList<Pair<String, Measure>>

    var nameString = MutableLiveData<String>()
    var durationString = MutableLiveData<String>()
    var descriptionString = MutableLiveData<String>()
    var instructionsString = MutableLiveData<String>()
    val mealTemplateList: MutableLiveData<MutableList<MealTemplate>> by lazy {
        MutableLiveData<MutableList<MealTemplate>>().apply {
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

    init {
        database.collection(Product.COLLECTION_NAME).get().addOnSuccessListener { documents ->
            val list = mutableListOf<Pair<String, Product>>()
            for (document in documents)
                list.add(Pair(document.id, document.toObject()))
            products.value = list
        }
        measures = MeasureRepository.measures
    }

    fun addMealTemplate(mealTemplate: MealTemplate) {
        val mealTemplateList = mealTemplateList.value
        mealTemplateList?.let { list ->
            list.firstOrNull { it.product == mealTemplate.product }?.apply {
                this.value += mealTemplate.value
            }
                ?: list.add(mealTemplate)
        }
        this.mealTemplateList.value = mealTemplateList
    }

    fun addNewMealToDatabase() {
        // TODO Check if name is null and consumptionList empty (Validation)
        viewModelScope.launch {
            val mealIngredientList = mealTemplateList.value!!.map { template ->
                MealIngredient(
                    database.collection(Product.COLLECTION_NAME)
                        .document(products.value!!.first { it.second.name == template.product.name }.first),
                    database.collection(Measure.COLLECTION_NAME).document(measures.first { it.second.name == template.value.measure.name}.first),
                    template.value.double
                )
            }
            database.collection(Meal.COLLECTION_NAME)
                .add(Meal(name!!, duration, description, instructions, photoFile.toString(), mealIngredientList))
        }
    }
}
