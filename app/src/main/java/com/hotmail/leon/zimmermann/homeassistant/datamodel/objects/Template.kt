package com.hotmail.leon.zimmermann.homeassistant.datamodel.objects

import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.DataIntegrityException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseTemplate
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseTemplateComponent

data class Template(
    var id: String,
    var name: String,
    var components: List<TemplateComponent>
) {
    companion object {
        fun createInstance(id: String, firebaseObject: FirebaseTemplate): Template {
            val name = firebaseObject.name ?: throw DataIntegrityException()
            val components = firebaseObject.components
                ?.map { TemplateComponent.createInstance(it) }
                ?: throw DataIntegrityException()
            return Template(id, name, components)
        }
    }
}

data class TemplateComponent(
    var productId: String,
    var measureId: String,
    var value: Double
) {
    companion object {
        fun createInstance(firebaseObject: FirebaseTemplateComponent): TemplateComponent {
            val productId = firebaseObject.product?.id ?: throw DataIntegrityException()
            val measureId = firebaseObject.measure?.id ?: throw DataIntegrityException()
            val value = firebaseObject.value ?: throw DataIntegrityException()
            return TemplateComponent(productId, measureId, value)
        }
    }
}