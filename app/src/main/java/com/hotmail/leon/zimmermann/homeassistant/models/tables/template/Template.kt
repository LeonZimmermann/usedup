package com.hotmail.leon.zimmermann.homeassistant.models.tables.template

import androidx.room.*
import java.io.Serializable

data class Template(
    @Embedded
    val metaData: TemplateMetaDataEntity,
    @Relation(parentColumn = "id", entityColumn = "list_id", entity = TemplateItemEntity::class)
    val templateItems: List<TemplateItemEntity>
) : Serializable