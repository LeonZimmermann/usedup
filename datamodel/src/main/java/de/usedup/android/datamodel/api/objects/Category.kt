package de.usedup.android.datamodel.api.objects

import de.usedup.android.datamodel.api.exceptions.DataIntegrityException
import de.usedup.android.datamodel.firebase.objects.FirebaseCategory
import de.usedup.android.datamodel.firebase.objects.FirebaseId
import java.util.*

class Category(
    val id: Id,
    private val _name: Map<String, String>,
    var position: Int
) {

    /**
     * The names for categories are stored in maps, with a language as the key and the according string as the value.
     * This enables the storage of category names in multiple languages. _name is the said map and the name field returns
     * the translation for the current Locale, or the english translation as a fallback.
     */
    val name: String
        get() = _name[Locale.getDefault().language] ?: requireNotNull(_name["en"])

    companion object {
        internal fun createInstance(id: String, firebaseObject: FirebaseCategory): Category {
            val name = firebaseObject.name ?: throw DataIntegrityException()
            val position = firebaseObject.position ?: throw DataIntegrityException()
            return Category(FirebaseId(id), name, position)
        }
    }
}