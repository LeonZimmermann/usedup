package com.hotmail.leon.zimmermann.homeassistant.models.tables.template

data class Template(
    val name: String,
    val items: List<TemplateItem>
) {
    constructor(templateWithItems: TemplateWithItems) : this(
        templateWithItems.template.name,
        templateWithItems.items.map {
            TemplateItem(it.productId, it.value, it.measureId)
        }
    )
}

data class TemplateItem(
    val productId: Long,
    val value: Double,
    val measureId: Long
)