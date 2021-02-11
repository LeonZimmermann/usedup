package de.usedup.android.datamodel.api.objects

import de.usedup.android.datamodel.api.exceptions.DataIntegrityException
import de.usedup.android.datamodel.firebase.objects.FirebaseId
import de.usedup.android.datamodel.firebase.objects.FirebaseTemplate
import de.usedup.android.datamodel.firebase.objects.FirebaseTemplateComponent

data class Template(
    val id: Id,
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