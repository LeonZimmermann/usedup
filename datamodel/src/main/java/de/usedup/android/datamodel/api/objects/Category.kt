package de.usedup.android.datamodel.api.objects

import de.usedup.android.datamodel.api.exceptions.DataIntegrityException
import de.usedup.android.datamodel.firebase.objects.FirebaseCategory
import de.usedup.android.datamodel.firebase.objects.FirebaseId

class Category(
    val id: Id,
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