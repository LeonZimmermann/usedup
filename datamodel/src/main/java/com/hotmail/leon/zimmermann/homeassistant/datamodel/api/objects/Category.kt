package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.exceptions.DataIntegrityException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseCategory
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseId

class Category(
    var id: Id,
    var name: String,
    var position: Int
) {
    companion object {
        internal fun createInstance(id: String, firebaseObject: FirebaseCategory): Category {
            val name = firebaseObject.name ?: throw DataIntegrityException()
            val position = firebaseObject.position ?: throw DataIntegrityException()
            return Category(FirebaseId(id), name, position)
        }
    }
}