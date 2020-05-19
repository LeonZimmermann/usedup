package com.hotmail.leon.zimmermann.homeassistant.datamodel

data class Template(
    val name: String,
    val items: List<TemplateItem>
)

data class TemplateItem(val product: Product, val measure: Measure, val value: Double)