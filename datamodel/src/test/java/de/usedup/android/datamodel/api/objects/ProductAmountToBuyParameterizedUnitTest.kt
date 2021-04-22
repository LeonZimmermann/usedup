package de.usedup.android.datamodel.api.objects

import de.usedup.android.datamodel.api.mockProduct
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ProductAmountToBuyParameterizedUnitTest(private val quantity: Double, private val max: Int,
  private val amountToBuy: Int) {

  @Test
  fun testAmountToBuy() {
    assertEquals(amountToBuy, mockProduct(quantity = quantity, max = max).amountToBuy)
  }

  private companion object {
    @JvmStatic
    @Parameterized.Parameters(
      name = "given a current quantity of {0} and a maximum quantity of {1}, when amountToBuy is called, then return {2}")
    fun arguments() = listOf(
      arrayOf(0, 3, 3),
      arrayOf(0.5, 3, 3),
      arrayOf(1, 3, 2),
      arrayOf(1.5, 3, 2),
      arrayOf(2, 3, 1),
      arrayOf(2.5, 3, 1),
      arrayOf(3, 3, 0),
      arrayOf(4, 3, 0),
    )
  }
}