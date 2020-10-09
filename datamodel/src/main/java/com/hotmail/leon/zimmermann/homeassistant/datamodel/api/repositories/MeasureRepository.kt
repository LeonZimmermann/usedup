package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Measure

interface MeasureRepository {
  val measures: MutableList<Measure>
  suspend fun init()
  fun getMeasureForId(id: Id): Measure
  fun getMeasureForName(name: String): Measure
}