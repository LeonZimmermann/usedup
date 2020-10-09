package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories

import androidx.lifecycle.MutableLiveData
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Meal
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.MealIngredient
import java.io.IOException

interface MealRepository {

  val meals: MutableLiveData<MutableList<Meal>>

  suspend fun init()

  @Throws(NoSuchElementException::class)
  suspend fun getMealForId(id: Id): Meal

  @Throws(NoSuchElementException::class)
  suspend fun getMealForName(name: String): Meal

  @Throws(IOException::class)
  suspend fun addMeal(name: String, duration: Int, description: String?, instructions: String?, backgroundUrl: String?,
    ingredients: List<MealIngredient>)

  @Throws(IOException::class, NoSuchElementException::class)
  suspend fun updateMeal(id: Id, name: String, duration: Int, description: String, instructions: String,
    backgroundUrl: String?, ingredients: List<MealIngredient>)

  @Throws(IOException::class, NoSuchElementException::class)
  suspend fun deleteMeal(id: Id)

}