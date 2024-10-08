package de.usedup.android.datamodel.api.repositories

import androidx.lifecycle.LiveData
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.Meal
import de.usedup.android.datamodel.api.objects.MealIngredient
import kotlinx.coroutines.CoroutineScope
import java.io.IOException

interface MealRepository {

  fun getAllMeals(coroutineScope: CoroutineScope): LiveData<Set<Meal>>

  suspend fun getMealForId(id: Id): Meal?

  suspend fun getMealForName(name: String): Meal?

  suspend fun addMeal(name: String, duration: Int, description: String?, instructions: String?, backgroundUrl: String?,
    ingredients: List<MealIngredient>)

  suspend fun updateMeal(id: Id, name: String, duration: Int, description: String?, instructions: String?,
    backgroundUrl: String?, ingredients: List<MealIngredient>)

  suspend fun deleteMeal(id: Id)

}