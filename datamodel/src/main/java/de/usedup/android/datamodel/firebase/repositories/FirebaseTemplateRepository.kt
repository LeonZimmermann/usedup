package de.usedup.android.datamodel.firebase.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.Template
import de.usedup.android.datamodel.api.objects.TemplateComponent
import de.usedup.android.datamodel.api.repositories.TemplateRepository
import de.usedup.android.datamodel.firebase.filterForUser
import de.usedup.android.datamodel.firebase.objects.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

object FirebaseTemplateRepository : TemplateRepository {
  private val collection = Firebase.firestore.collection(FirebaseTemplate.COLLECTION_NAME)

  private val templateList: MutableLiveData<Set<Template>> = MutableLiveData()

  override fun getAllTemplates(coroutineScope: CoroutineScope): LiveData<Set<Template>> {
    if (templateList.value == null) {
      coroutineScope.launch(Dispatchers.IO) {
        templateList.postValue(
          Tasks.await(collection.filterForUser().get()).map { Template.createInstance(it.id, it.toObject()) }.toSet())
      }
    }
    return templateList
  }

  override suspend fun getTemplateForId(id: Id) = withContext(Dispatchers.IO) {
    val document = Tasks.await(collection.document((id as FirebaseId).value).get())
    if (document.exists()) {
      val firebaseTemplate = document.toObject<FirebaseTemplate>() ?: throw IOException()
      Template.createInstance(document.id, firebaseTemplate)
    } else {
      null
    }
  }

  override suspend fun getTemplateForName(name: String) = withContext(Dispatchers.IO) {
    val document = Tasks.await(collection.filterForUser().whereEqualTo("name", name).get()).first()
    if (document.exists()) {
      val firebaseTemplate = document.toObject<FirebaseTemplate>()
      Template.createInstance(document.id, firebaseTemplate)
    } else {
      null
    }
  }

  override suspend fun addTemplate(name: String, components: List<TemplateComponent>): Unit =
    withContext(Dispatchers.IO) {
      val firebaseComponents = mapComponents(components)
      val firebaseTemplate =
        FirebaseTemplate(name, firebaseComponents)
      val task = collection.add(firebaseTemplate).apply { Tasks.await(this) }
      when {
        task.exception != null -> {
          throw IOException(task.exception)
        }
        task.result == null -> {
          throw IOException()
        }
        else -> {
          val mutableTemplateSet = requireNotNull(templateList.value).toMutableSet()
          mutableTemplateSet.add(Template.createInstance(requireNotNull(task.result).id, firebaseTemplate))
          templateList.postValue(mutableTemplateSet.toSet())
        }
      }
    }

  // TODO Add filter for user
  override suspend fun updateTemplate(id: Id, name: String, components: List<TemplateComponent>) {
    withContext(Dispatchers.IO) {
      val firebaseComponents = mapComponents(components)
      val data = mapOf(
        "name" to name,
        "components" to firebaseComponents
      )
      val task = collection.document((id as FirebaseId).value).update(data).apply { Tasks.await(this) }
      if (task.exception != null) throw IOException(task.exception!!)
      else {
        getTemplateForId(id)?.apply {
          this.name = name
          this.components = components
          templateList.postValue(templateList.value)
        }
      }
    }
  }

  override suspend fun deleteTemplate(id: Id): Unit = withContext(Dispatchers.IO) {
    // TODO Retry on failure
    collection.document((id as FirebaseId).value).delete()
  }

  private fun mapComponents(components: List<TemplateComponent>) = components.map {
    FirebaseTemplateComponent(
      Firebase.firestore.collection(FirebaseProduct.COLLECTION_NAME).document((it.productId as FirebaseId).value),
      Firebase.firestore.collection(FirebaseMeasure.COLLECTION_NAME).document((it.measureId as FirebaseId).value),
      it.value
    )
  }
}