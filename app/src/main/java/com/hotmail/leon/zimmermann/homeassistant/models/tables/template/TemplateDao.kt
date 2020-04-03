package com.hotmail.leon.zimmermann.homeassistant.models.tables.template

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class TemplateDao {

    @Query("SELECT * FROM templates")
    abstract fun getAll(): LiveData<List<TemplateWithItems>>

    suspend fun insert(template: Template) {
        val templateId = insert(TemplateEntity(template.name))
        val items = template.items.map { TemplateItemEntity(templateId, it.productId, it.value, it.measureId) }
        insert(items)
    }

    @Insert
    protected abstract suspend fun insert(templateItemEntityList: List<TemplateItemEntity>)

    @Insert
    protected abstract suspend fun insert(templateEntity: TemplateEntity): Long

}