package com.hotmail.leon.zimmermann.homeassistant.datamodel.objects

import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.DataIntegrityException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.internal.FirebaseCategory

class Category(
    var id: String,
    var name: String,
    var position: Int
) {
    companion object {
        fun createInstance(id: String, firebaseObject: FirebaseCategory): Category {
            val name = firebaseObject.name ?: throw DataIntegrityException()
            val position = firebaseObject.position ?: throw DataIntegrityException()
            return Category(id, name, position)
        }
    }
}