package com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Template
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.TemplateComponent
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.TemplateRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.filterForUser
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object FirebaseTemplateRepository : TemplateRepository {
  private val collection = Firebase.firestore.collection(FirebaseTemplate.COLLECTION_NAME)

  override val templates: MutableLiveData<MutableList<Template>> = MutableLiveData()

  override suspend fun init() {
    withContext(Dispatchers.IO) {
      collection.filterForUser().get()
        .addOnSuccessListener { documents ->
          templates.value = documents.map { Template.createInstance(it.id, it.toObject()) }.toMutableList()
        }
    }
  }

  @Throws(NoSuchElementException::class)
  override suspend fun getTemplateForId(id: Id) = withContext(Dispatchers.IO) {
    if (templates.value != null) templates.value!!.first { it.id == id }
    else {
      val document = Tasks.await(collection.document((id as FirebaseId).value).get())
      val firebaseTemplate = document.toObject<FirebaseTemplate>() ?: throw IOException()
      val template = Template.createInstance(document.id, firebaseTemplate)
      val templateList = templates.value!!
      templateList.add(template)
      templates.postValue(templateList)
      template
    }
  }

  @Throws(NoSuchElementException::class)
  override suspend fun getTemplateForName(name: String) = withContext(Dispatchers.IO) {
    if (templates.value != null) templates.value!!.first { it.name == name }
    else {
      val document = Tasks.await(collection.filterForUser().whereEqualTo("name", name).get()).first()
      val firebaseTemplate = document.toObject<FirebaseTemplate>()
      val template = Template.createInstance(document.id, firebaseTemplate)
      val templateList = templates.value!!
      templateList.add(template)
      templates.postValue(templateList)
      template
    }
  }

  @Throws(IOException::class)
  override suspend fun addTemplate(name: String, components: List<TemplateComponent>) = withContext(Dispatchers.IO) {
    val firebaseComponents = mapComponents(components)
    val firebaseTemplate = FirebaseTemplate(name, firebaseComponents)
    val task = collection.add(firebaseTemplate).apply { Tasks.await(this) }
    if (task.exception != null) throw IOException(task.exception!!)
    else {
      val templateList = templates.value!!
      templateList.add(Template.createInstance(task.result!!.id, firebaseTemplate))
      templates.postValue(templateList)
    }
  }

  // TODO Add filter for user
  @Throws(IOException::class, NoSuchElementException::class)
  override suspend fun updateTemplate(id: Id, name: String, components: List<TemplateComponent>) =
    withContext(Dispatchers.IO) {
      val firebaseComponents = mapComponents(components)
      val data = mapOf(
        "name" to name,
        "components" to firebaseComponents
      )
      val task = collection.document((id as FirebaseId).value).update(data).apply { Tasks.await(this) }
      if (task.exception != null) throw IOException(task.exception!!)
      else {
        getTemplateForId(id).apply {
          this.name = name
          this.components = components
        }
        templates.postValue(templates.value)
      }
    }

  @Throws(IOException::class, NoSuchElementException::class)
  override suspend fun deleteTemplate(id: Id) = withContext(Dispatchers.IO) {
    templates.value!!.remove(getTemplateForId(id))
    val task = collection.document((id as FirebaseId).value).delete().apply { Tasks.await(this) }
    if (task.exception != null) throw IOException(task.exception!!)
    else templates.postValue(templates.value)
  }

  private fun mapComponents(components: List<TemplateComponent>) = components.map {
    FirebaseTemplateComponent(
      Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME).document((it.productId as FirebaseId).value),
      Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).document((it.measureId as FirebaseId).value),
      it.value
    )
  }
}