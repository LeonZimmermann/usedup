package de.usedup.android.consumption

import com.usedup.android.zimmermann.homeassistant.datamodel.api.objects.MeasureValue
import com.usedup.android.zimmermann.homeassistant.datamodel.api.repositories.MeasureRepository
import de.usedup.android.app.*
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConsumptionCalculatorUnitTest {

  @MockK
  private lateinit var measureRepository: MeasureRepository

  @InjectMockKs
  private lateinit var testee: ConsumptionCalculator

  @Before
  fun setUp() {
    MockKAnnotations.init(this, relaxUnitFun = true)
    initMeasureRepositoryForMocks(measureRepository)
  }

  @Test
  fun `given same measures and whole quantity, when calculateUpdatedQuantity() is called, then calculate correct quantity`() {
    val product = mockProduct(quantity = 1.0, capacity = 200.0, measureId = MEASURE_TYPE_1_BASE_1.id)
    val measureValue = MeasureValue(20.0, MEASURE_TYPE_1_BASE_1)

    val result = testee.calculateUpdatedQuantity(product, measureValue)

    assertEquals(0.9, result, 0.00001)
  }

  @Test
  fun `given measures with same type and whole quantity, when calculateUpdatedQuantity() is called, then calculate correct quantity`() {
    val product = mockProduct(quantity = 1.0, capacity = 2000.0, measureId = MEASURE_TYPE_1_BASE_0_1.id)
    val measureValue = MeasureValue(10.0, MEASURE_TYPE_1_BASE_10)

    val result = testee.calculateUpdatedQuantity(product, measureValue)

    assertEquals(.5, result, 0.00001)
  }

  @Test
  fun `given measures with same type and non whole quantity, when calculateUpdatedQuantity() is called, then calculate correct quantity`() {
    val product = mockProduct(quantity = 1.5, capacity = 2000.0, measureId = MEASURE_TYPE_1_BASE_0_1.id)
    val measureValue = MeasureValue(10.0, MEASURE_TYPE_1_BASE_10)

    val result = testee.calculateUpdatedQuantity(product, measureValue)

    assertEquals(1.0, result, 0.00001)
  }

  @Test
  fun `given measures with different types, when calculateUpdatedQuantity() is called, then throw ConsumptionException`() {
    val product = mockProduct(quantity = 1.5, capacity = 200.0, measureId = MEASURE_TYPE_1_BASE_1.id)
    val measureValue = MeasureValue(10.0, MEASURE_TYPE_2_BASE_1)

    assertThatThrownBy { testee.calculateUpdatedQuantity(product, measureValue) }.isInstanceOf(
      ConsumptionException::class.java)
  }

  @Test
  fun `given not enough quantity, when calculateUpdatedQuantity() is called, then throw NotEnoughException`() {
    val product = mockProduct(quantity = 1.5, capacity = 20.0, measureId = MEASURE_TYPE_1_BASE_1.id)
    val measureValue = MeasureValue(10.0, MEASURE_TYPE_1_BASE_10)

    assertThatThrownBy { testee.calculateUpdatedQuantity(product, measureValue) }.isInstanceOf(
      NotEnoughException::class.java)
  }
}