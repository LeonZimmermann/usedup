package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories

import androidx.lifecycle.MutableLiveData
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Template
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.TemplateComponent
import java.io.IOException

interface TemplateRepository {

  val templates: MutableLiveData<MutableList<Template>>

  suspend fun init()

  @Throws(NoSuchElementException::class)
  suspend fun getTemplateForId(id: Id): Template

  @Throws(NoSuchElementException::class)
  suspend fun getTemplateForName(name: String): Template

  @Throws(IOException::class)
  suspend fun addTemplate(name: String, components: List<TemplateComponent>)

  @Throws(IOException::class, NoSuchElementException::class)
  suspend fun updateTemplate(id: Id, name: String, components: List<TemplateComponent>)

  @Throws(IOException::class, NoSuchElementException::class)
  suspend fun deleteTemplate(id: Id)
}