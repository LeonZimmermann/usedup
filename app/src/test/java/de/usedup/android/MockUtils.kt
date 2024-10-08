package de.usedup.android

import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.Measure
import de.usedup.android.datamodel.api.objects.Product
import de.usedup.android.datamodel.api.repositories.MeasureRepository
import io.mockk.every
import io.mockk.mockk

fun mockProduct(name: String = "", quantity: Double = 0.0, min: Int = 0, max: Int = 0,
  capacity: Double = 0.0, measureId: Id = mockk(), categoryId: Id = mockk()): Product =
  Product(mockk(name), name, quantity, min, max, capacity, measureId, categoryId)

fun mockMeasure(name: String = "", abbreviation: String = "", baseFactor: Float = .0f, type: String = "", complex: Boolean = true): Measure =
  Measure(mockk(name), name, abbreviation, baseFactor, type, complex)

fun initMeasureRepositoryForMocks(measureRepository: MeasureRepository) {
  every { measureRepository.getMeasureForId(MEASURE_TYPE_1_BASE_0_1.id) } returns MEASURE_TYPE_1_BASE_0_1
  every { measureRepository.getMeasureForId(MEASURE_TYPE_1_BASE_1.id) } returns MEASURE_TYPE_1_BASE_1
  every { measureRepository.getMeasureForId(MEASURE_TYPE_1_BASE_10.id) } returns MEASURE_TYPE_1_BASE_10

  every { measureRepository.getMeasureForId(MEASURE_TYPE_2_BASE_0_1.id) } returns MEASURE_TYPE_2_BASE_0_1
  every { measureRepository.getMeasureForId(MEASURE_TYPE_2_BASE_1.id) } returns MEASURE_TYPE_2_BASE_1
  every { measureRepository.getMeasureForId(MEASURE_TYPE_2_BASE_10.id) } returns MEASURE_TYPE_2_BASE_10
}

val MEASURE_TYPE_1_BASE_0_1 = mockMeasure(name = "MEASURE_TYPE_1_BASE_0_1", baseFactor = .1f, type = "Type1")
val MEASURE_TYPE_1_BASE_1 = mockMeasure(name = "MEASURE_TYPE_1_BASE_1", baseFactor = 1f, type = "Type1")
val MEASURE_TYPE_1_BASE_10 = mockMeasure(name = "MEASURE_TYPE_1_BASE_10", baseFactor = 10f, type = "Type1")

val MEASURE_TYPE_2_BASE_0_1 = mockMeasure(name = "MEASURE_TYPE_2_BASE_0_1", baseFactor = .1f, type = "Type2")
val MEASURE_TYPE_2_BASE_1 = mockMeasure(name = "MEASURE_TYPE_2_BASE_1", baseFactor = 1f, type = "Type2")
val MEASURE_TYPE_2_BASE_10 = mockMeasure(name = "MEASURE_TYPE_2_BASE_10", baseFactor = 10f, type = "Type2")
