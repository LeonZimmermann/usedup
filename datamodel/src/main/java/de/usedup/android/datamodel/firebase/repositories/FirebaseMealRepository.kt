package de.usedup.android.datamodel.firebase.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.Meal
import de.usedup.android.datamodel.api.objects.MealIngredient
import de.usedup.android.datamodel.api.repositories.MealRepository
import de.usedup.android.datamodel.firebase.filterForUser
import de.usedup.android.datamodel.firebase.objects.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

object FirebaseMealRepository : MealRepository {
  private val collection = Firebase.firestore.collection(FirebaseMeal.COLLECTION_NAME)

  private val mealList: MutableLiveData<Set<Meal>> = MutableLiveData()

  override fun getAllMeals(coroutineScope: CoroutineScope): LiveData<Set<Meal>> {
    if (mealList.value == null) {
      coroutineScope.launch(Dispatchers.IO) {
        mealList.postValue(
          Tasks.await(collection.filterForUser().get()).map { Meal.createInstance(it.id, it.toObject()) }.toSet())
      }
    }
    return mealList
  }

  override suspend fun getMealForId(id: Id): Meal? = withContext(Dispatchers.IO) {
    val document = Tasks.await(collection.document((id as FirebaseId).value).get())
    if (document.exists()) {
      val firebaseMeal = document.toObject<FirebaseMeal>() ?: throw IOException()
      Meal.createInstance(document.id, firebaseMeal)
    } else {
      null
    }
  }

  override suspend fun getMealForName(name: String) = withContext(Dispatchers.IO) {
    val document = Tasks.await(collection.filterForUser().whereEqualTo("name", name).get()).first()
    if (document.exists()) {
      val firebaseMeal = document.toObject<FirebaseMeal>()
      Meal.createInstance(document.id, firebaseMeal)
    } else {
      null
    }
  }

  override suspend fun addMeal(
    name: String,
    duration: Int,
    description: String?,
    instructions: String?,
    backgroundUrl: String?,
    ingredients: List<MealIngredient>
  ): Unit = withContext(Dispatchers.IO) {
    val firebaseIngredients = mapMealIngredients(ingredients)
    val firebaseMeal = FirebaseMeal(name, duration, description, instructions, backgroundUrl, firebaseIngredients)
    val task = collection.add(firebaseMeal).apply { Tasks.await(this) }
    when {
      task.exception != null -> {
        throw IOException(task.exception)
      }
      task.result == null -> {
        throw IOException()
      }
      else -> {
        val mutableMealSet = requireNotNull(mealList.value).toMutableSet()
        mutableMealSet.add(Meal.createInstance(requireNotNull(task.result).id, firebaseMeal))
        mealList.postValue(mutableMealSet.toSet())
      }
    }
  }

  override suspend fun updateMeal(
    id: Id,
    name: String,
    duration: Int,
    description: String,
    instructions: String,
    backgroundUrl: String?,
    ingredients: List<MealIngredient>
  ) {
    withContext(Dispatchers.IO) {
      val firebaseIngredients = mapMealIngredients(ingredients)
      val data = mapOf(
        "name" to name,
        "duration" to duration,
        "description" to description,
        "name" to instructions,
        "backgroundUrl" to backgroundUrl,
        "ingredients" to firebaseIngredients
      )
      val task = collection.document((id as FirebaseId).value).update(data).apply { Tasks.await(this) }
      if (task.exception != null) throw IOException(requireNotNull(task.exception))
      else {
        getMealForId(id)?.apply {
          this.name = name
          this.duration = duration
          this.description = description
          this.instructions = instructions
          this.backgroundUrl = backgroundUrl
          this.ingredients = ingredients
          mealList.postValue(mealList.value)
        }
      }
    }
  }

  override suspend fun deleteMeal(id: Id): Unit = withContext(Dispatchers.IO) {
    // TODO Warn user that planner items containing this meal will be deleted
    // TODO Retry on failure
    val mealReference = collection.document((id as FirebaseId).value)
    mealReference.delete().addOnSuccessListener {
      Firebase.firestore.collection(FirebasePlannerItem.COLLECTION_NAME).whereEqualTo("mealReference", mealReference)
        .addSnapshotListener { value, error ->
          if (error != null) {
            value?.documents?.map { it.reference }?.forEach { it.delete() }
          } else {
            // TODO Add logging and retry
          }
        }
    }
  }

  private fun mapMealIngredients(ingredients: List<MealIngredient>) = ingredients.map {
    FirebaseMealIngredient(
      Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME).document((it.productId as FirebaseId).value),
      Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).document((it.measureId as FirebaseId).value),
      it.value
    )
  }
}