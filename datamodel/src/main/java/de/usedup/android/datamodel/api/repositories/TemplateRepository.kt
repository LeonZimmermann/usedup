package de.usedup.android.datamodel.api.repositories

import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.Template
import de.usedup.android.datamodel.api.objects.TemplateComponent
import java.io.IOException

interface TemplateRepository {

  fun getAllTemplates(): Set<Template>

  suspend fun getTemplateForId(id: Id): Template?

  suspend fun getTemplateForName(name: String): Template?

  suspend fun addTemplate(name: String, components: List<TemplateComponent>)

  suspend fun updateTemplate(id: Id, name: String, components: List<TemplateComponent>)

  suspend fun deleteTemplate(id: Id)
}