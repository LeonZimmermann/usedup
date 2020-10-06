package com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseMeasure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseTemplate
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseTemplateComponent
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Template
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.TemplateComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object TemplateRepository {
  private val database = Firebase.firestore

  val templates: MutableLiveData<MutableList<Template>> = MutableLiveData()

  init {
    database.collection(FirebaseTemplate.COLLECTION_NAME).get().addOnSuccessListener { documents ->
      templates.value = documents.map { Template.createInstance(it.id, it.toObject()) }.toMutableList()
    }
  }

  @Throws(NoSuchElementException::class)
  suspend fun getTemplateForId(id: String) = withContext(Dispatchers.IO) {
    if (templates.value != null) templates.value!!.first { it.id == id }
    else {
      val document = Tasks.await(database.collection(FirebaseTemplate.COLLECTION_NAME).document(id).get())
      val firebaseTemplate = document.toObject<FirebaseTemplate>() ?: throw IOException()
      val template = Template.createInstance(document.id, firebaseTemplate)
      val templateList = templates.value!!
      templateList.add(template)
      templates.postValue(templateList)
      template
    }
  }

  @Throws(NoSuchElementException::class)
  suspend fun getTemplateForName(name: String) = withContext(Dispatchers.IO) {
    if (templates.value != null) templates.value!!.first { it.name == name }
    else {
      val document =
        Tasks.await(database.collection(FirebaseTemplate.COLLECTION_NAME).whereEqualTo("name", name).get()).first()
      val firebaseTemplate = document.toObject<FirebaseTemplate>()
      val template = Template.createInstance(document.id, firebaseTemplate)
      val templateList = templates.value!!
      templateList.add(template)
      templates.postValue(templateList)
      template
    }
  }

  @Throws(IOException::class)
  suspend fun addTemplate(name: String, components: List<TemplateComponent>) = withContext(Dispatchers.IO) {
    val firebaseComponents = components.map {
      FirebaseTemplateComponent(
        Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME).document(it.productId),
        Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).document(it.measureId),
        it.value
      )
    }
    val firebaseTemplate = FirebaseTemplate(name, firebaseComponents)
    val task = database.collection(FirebaseTemplate.COLLECTION_NAME).add(firebaseTemplate)
    Tasks.await(task)
    if (task.exception != null) throw IOException(task.exception!!)
    else {
      val templateList = templates.value!!
      templateList.add(Template.createInstance(task.result!!.id, firebaseTemplate))
      templates.postValue(templateList)
    }
  }

  @Throws(IOException::class, NoSuchElementException::class)
  suspend fun updateTemplate(templateId: String, name: String, components: List<TemplateComponent>) =
    withContext(Dispatchers.IO) {
      val firebaseComponents = components.map {
        FirebaseTemplateComponent(
          Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME).document(it.productId),
          Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).document(it.measureId),
          it.value
        )
      }
      val data = mapOf(
        "name" to name,
        "components" to firebaseComponents
      )
      val task = database.collection(FirebaseTemplate.COLLECTION_NAME)
        .document(templateId)
        .update(data)
      Tasks.await(task)
      if (task.exception != null) throw IOException(task.exception!!)
      else {
        getTemplateForId(templateId).apply {
          this.name = name
          this.components = components
        }
        templates.postValue(templates.value)
      }
    }

  @Throws(IOException::class, NoSuchElementException::class)
  suspend fun deleteTemplate(templateId: String) = withContext(Dispatchers.IO) {
    templates.value!!.remove(getTemplateForId(templateId))
    val task = database.collection(FirebaseTemplate.COLLECTION_NAME).document(templateId).delete()
    Tasks.await(task)
    if (task.exception != null) throw IOException(task.exception!!)
    else templates.postValue(templates.value)
  }
}