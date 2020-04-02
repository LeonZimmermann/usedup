package com.hotmail.leon.zimmermann.homeassistant.models.tables.template

class TemplateRepository(private val templateDao: TemplateDao) {

    val templateList = templateDao.getAll()

    suspend fun insert(template: Template) {
        templateDao.insert(template)
    }
}