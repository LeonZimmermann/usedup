package de.usedup.android.datamodel.api.repositories

import androidx.lifecycle.MutableLiveData
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.Template
import de.usedup.android.datamodel.api.objects.TemplateComponent
import java.io.IOException

interface TemplateRepository {

  val templates: MutableLiveData<MutableList<Template>>

  suspend fun init()

  suspend fun getTemplateForId(id: Id): Template?

  suspend fun getTemplateForName(name: String): Template?

  @Throws(IOException::class)
  suspend fun addTemplate(name: String, components: List<TemplateComponent>)

  @Throws(IOException::class)
  suspend fun updateTemplate(id: Id, name: String, components: List<TemplateComponent>)

  @Throws(IOException::class)
  suspend fun deleteTemplate(id: Id)
}