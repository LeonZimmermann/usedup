package com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object MealRepository {
  private val database = Firebase.firestore

  val meals: MutableLiveData<MutableList<Meal>> = MutableLiveData()

  init {
    database.collection(FirebaseMeal.COLLECTION_NAME).get().addOnSuccessListener { documents ->
      meals.value = documents.map { Meal.createInstance(it.id, it.toObject()) }.toMutableList()
    }
  }

  @Throws(NoSuchElementException::class)
  suspend fun getMealForId(id: String): Meal = withContext(Dispatchers.IO) {
    if (meals.value != null) meals.value!!.first { it.id == id }
    else {
      val document = Tasks.await(
          database.collection(FirebaseMeal.COLLECTION_NAME).document(id).get())
      val firebaseMeal = document.toObject<FirebaseMeal>() ?: throw IOException()
      val meal = Meal.createInstance(document.id, firebaseMeal)
      val mealList = meals.value!!
      mealList.add(meal)
      meals.postValue(mealList)
      meal
    }
  }

  @Throws(NoSuchElementException::class)
  suspend fun getMealForName(name: String): Meal = withContext(Dispatchers.IO) {
    if (meals.value != null) meals.value!!.first { it.name == name }
    else {
      val document = Tasks.await(
          database.collection(FirebaseMeal.COLLECTION_NAME).whereEqualTo("name", name).get()).first()
      val firebaseMeal = document.toObject<FirebaseMeal>()
      val meal = Meal.createInstance(document.id, firebaseMeal)
      val mealList = meals.value!!
      mealList.add(meal)
      meals.postValue(mealList)
      meal
    }
  }

  @Throws(IOException::class)
  suspend fun addMeal(
      name: String,
      duration: Int,
      description: String?,
      instructions: String?,
      backgroundUrl: String?,
      ingredients: List<MealIngredient>
  ) = withContext(Dispatchers.IO) {
    val firebaseIngredients = ingredients.map {
      FirebaseMealIngredient(
          Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME).document(it.productId),
          Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).document(it.measureId),
          it.value
      )
    }
    val firebaseMeal = FirebaseMeal(name, duration, description, instructions, backgroundUrl, firebaseIngredients)
    val task = database.collection(FirebaseMeal.COLLECTION_NAME).add(firebaseMeal)
    Tasks.await(task)
    if (task.exception != null) throw IOException(task.exception!!)
    else {
      val mealList = meals.value!!
      mealList.add(
          Meal.createInstance(task.result!!.id, FirebaseMeal(name, duration, description, instructions, backgroundUrl)))
      meals.postValue(mealList)
    }
  }

  @Throws(IOException::class)
  suspend fun updateMeal(
      mealId: String,
      name: String,
      duration: Int,
      description: String,
      instructions: String,
      backgroundUrl: String?,
      ingredients: List<MealIngredient>
  ) = withContext(Dispatchers.IO) {
    val firebaseIngredients = ingredients.map {
      FirebaseMealIngredient(
          Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME).document(it.productId),
          Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).document(it.measureId),
          it.value
      )
    }
    val data = mapOf(
        "name" to name,
        "duration" to duration,
        "description" to description,
        "name" to instructions,
        "backgroundUrl" to backgroundUrl,
        "ingredients" to firebaseIngredients
    )
    val task = database.collection(FirebaseMeal.COLLECTION_NAME)
      .document(mealId)
      .update(data)
    Tasks.await(task)
    if (task.exception != null) throw IOException(task.exception!!)
    else {
      getMealForId(mealId).apply {
        this.name = name
        this.duration = duration
        this.description = description
        this.instructions = instructions
        this.backgroundUrl = backgroundUrl
        this.ingredients = ingredients
      }
      meals.postValue(meals.value)
    }
  }

  @Throws(IOException::class, NoSuchElementException::class)
  suspend fun deleteMeal(mealId: String) = withContext(Dispatchers.IO) {
    meals.value!!.remove(getMealForId(mealId))
    val task = database.collection(FirebaseMeal.COLLECTION_NAME).document(mealId).delete()
    Tasks.await(task)
    if (task.exception != null) throw IOException(task.exception!!)
    else meals.postValue(meals.value)
  }
}