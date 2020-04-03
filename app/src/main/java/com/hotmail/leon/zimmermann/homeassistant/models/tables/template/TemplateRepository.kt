package com.hotmail.leon.zimmermann.homeassistant.models.tables.template

import androidx.lifecycle.Transformations

class TemplateRepository(private val templateDao: TemplateDao) {

    val templateList = Transformations.map(templateDao.getAll()) { templateList -> templateList.map { Template(it) } }

    suspend fun insert(template: Template) {
        templateDao.insert(template)
    }
}