package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.exceptions.DataIntegrityException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseId
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseTemplate
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseTemplateComponent

data class Template(
    var id: Id,
    var name: String,
    var components: List<TemplateComponent>
) {
    companion object {
        internal fun createInstance(id: String, firebaseObject: FirebaseTemplate): Template {
            val name = firebaseObject.name ?: throw DataIntegrityException()
            val components = firebaseObject.components
                ?.map { TemplateComponent.createInstance(it) }
                ?: throw DataIntegrityException()
            return Template(FirebaseId(id), name, components)
        }
    }
}

data class TemplateComponent(
    var productId: Id,
    var measureId: Id,
    var value: Double
) {
    internal companion object {
        fun createInstance(firebaseObject: FirebaseTemplateComponent): TemplateComponent {
            val productId = FirebaseId(firebaseObject.product?.id ?: throw DataIntegrityException())
            val measureId = FirebaseId(firebaseObject.measure?.id ?: throw DataIntegrityException())
            val value = firebaseObject.value ?: throw DataIntegrityException()
            return TemplateComponent(productId, measureId, value)
        }
    }
}