package com.hotmail.leon.zimmermann.homeassistant.models.tables.template

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class TemplateDao {

    @Query("SELECT * FROM templates")
    abstract fun getAll(): LiveData<List<Template>>

    suspend fun insert(template: Template) {
        insert(template.metaData)
        insert(template.templateItems)
    }

    @Insert
    protected abstract suspend fun insert(templateItemEntityList: List<TemplateItemEntity>)

    @Insert
    protected abstract suspend fun insert(templateMetaDataEntity: TemplateMetaDataEntity)

}