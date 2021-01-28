package com.hotmail.leon.zimmermann.homeassistant.datamodel.api

import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class CalculateDiscrepancyParameterizedUnitTest(private val quantity: Double, private val min: Int,
  private val discrepancy: Int) {

  @Test
  fun testCalculateDiscrepancy() {
    assertEquals(discrepancy, calculateDiscrepancy(min, quantity))
  }

  private companion object {
    @JvmStatic
    @Parameterized.Parameters(name = "{0} for minimum of {1} = {2}")
    fun arguments() = listOf(
      arrayOf(0, 2, 2),
      arrayOf(0.111, 2, 2),
      arrayOf(0.999, 2, 2),
      arrayOf(1.0, 2, 1),
      arrayOf(1.111, 2, 1),
      arrayOf(1.999, 2, 1),
      arrayOf(2.0, 2, 0),
      arrayOf(2.111, 2, 0),
      arrayOf(2.999, 2, 0))
  }
}