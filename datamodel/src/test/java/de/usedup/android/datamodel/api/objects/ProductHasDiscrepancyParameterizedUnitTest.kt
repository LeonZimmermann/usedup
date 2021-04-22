package de.usedup.android.datamodel.api.objects

import de.usedup.android.datamodel.api.mockProduct
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ProductHasDiscrepancyParameterizedUnitTest(private val quantity: Double, private val min: Int,
  private val hasDiscrepancy: Boolean) {

  @Test
  fun testAmountToBuy() {
    assertEquals(hasDiscrepancy, mockProduct(quantity = quantity, min = min).hasDiscrepancy)
  }

  private companion object {
    @JvmStatic
    @Parameterized.Parameters(
      name = "given a current quantity of {0} and a minimum quantity of {1}, when hasDiscrepancy is called, then return {2}")
    fun arguments() = listOf(
      arrayOf(2, 3, true),
      arrayOf(3, 3, false),
      arrayOf(4, 3, false),
    )
  }
}