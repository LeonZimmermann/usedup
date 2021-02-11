package de.usedup.android.datamodel.firebase.repositories

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object FirebaseMealRepository : MealRepository {
  private val collection = Firebase.firestore.collection(FirebaseMeal.COLLECTION_NAME)

  override val meals: MutableLiveData<MutableList<Meal>> = MutableLiveData()

  override suspend fun init() {
    withContext(Dispatchers.IO) {
      collection.filterForUser().get()
        .addOnSuccessListener { documents ->
          meals.value = documents.map { Meal.createInstance(it.id, it.toObject()) }.toMutableList()
        }
    }
  }

  @Throws(NoSuchElementException::class)
  override suspend fun getMealForId(id: Id): Meal = withContext(Dispatchers.IO) {
    if (meals.value != null) meals.value!!.first { it.id == id }
    else {
      val document = Tasks.await(collection.document((id as FirebaseId).value).get())
      val firebaseMeal = document.toObject<FirebaseMeal>() ?: throw IOException()
      val meal = Meal.createInstance(document.id, firebaseMeal)
      val mealList = meals.value!!
      mealList.add(meal)
      meals.postValue(mealList)
      meal
    }
  }

  @Throws(NoSuchElementException::class)
  override suspend fun getMealForName(name: String): Meal = withContext(Dispatchers.IO) {
    if (meals.value != null) meals.value!!.first { it.name == name }
    else {
      val document = Tasks.await(collection.filterForUser().whereEqualTo("name", name).get()).first()
      val firebaseMeal = document.toObject<FirebaseMeal>()
      val meal = Meal.createInstance(document.id, firebaseMeal)
      val mealList = meals.value!!
      mealList.add(meal)
      meals.postValue(mealList)
      meal
    }
  }

  @Throws(IOException::class)
  override suspend fun addMeal(
    name: String,
    duration: Int,
    description: String?,
    instructions: String?,
    backgroundUrl: String?,
    ingredients: List<MealIngredient>
  ) = withContext(Dispatchers.IO) {
    val firebaseIngredients = mapMealIngredients(ingredients)
    val firebaseMeal = FirebaseMeal(name, duration, description, instructions, backgroundUrl, firebaseIngredients,
      FirebaseUserRepository.getDocumentReferenceToCurrentUser())
    val task = collection.add(firebaseMeal).apply { Tasks.await(this) }
    if (task.exception != null) throw IOException(task.exception!!)
    else {
      val mealList = meals.value!!
      mealList.add(Meal.createInstance(task.result!!.id, firebaseMeal))
      meals.postValue(mealList)
    }
  }

  @Throws(IOException::class, NoSuchElementException::class)
  override suspend fun updateMeal(
    id: Id,
    name: String,
    duration: Int,
    description: String,
    instructions: String,
    backgroundUrl: String?,
    ingredients: List<MealIngredient>
  ) = withContext(Dispatchers.IO) {
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
    if (task.exception != null) throw IOException(task.exception!!)
    else {
      getMealForId(id).apply {
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
  override suspend fun deleteMeal(id: Id) = withContext(Dispatchers.IO) {
    meals.value!!.remove(getMealForId(id))
    val task = collection.document((id as FirebaseId).value).delete().apply { Tasks.await(this) }
    if (task.exception != null) throw IOException(task.exception!!)
    else meals.postValue(meals.value)
  }

  private fun mapMealIngredients(ingredients: List<MealIngredient>) = ingredients.map {
    FirebaseMealIngredient(
      Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME).document((it.productId as FirebaseId).value),
      Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).document((it.measureId as FirebaseId).value),
      it.value
    )
  }
}