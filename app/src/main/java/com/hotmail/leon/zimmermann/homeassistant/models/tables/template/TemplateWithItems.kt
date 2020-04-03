package com.hotmail.leon.zimmermann.homeassistant.models.tables.template

import androidx.room.*
import java.io.Serializable

data class TemplateWithItems(
    @Embedded
    val template: TemplateEntity,
    @Relation(parentColumn = "template_id", entityColumn = "template_id", entity = TemplateItemEntity::class)
    val items: List<TemplateItemEntity>
) : Serializable