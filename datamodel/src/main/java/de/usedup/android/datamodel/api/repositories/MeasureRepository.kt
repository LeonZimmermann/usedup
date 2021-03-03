package de.usedup.android.datamodel.api.repositories

import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.Measure

interface MeasureRepository {
  val measures: MutableList<Measure>
  suspend fun init()
  fun getMeasureForId(id: Id): Measure?
  fun getMeasureForName(name: String): Measure?
}