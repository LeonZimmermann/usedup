package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Measure

interface MeasureRepository {
  fun init()
  val measures: MutableList<Measure>
  fun getMeasureForId(id: Id): Measure
  fun getMeasureForName(name: String): Measure
}