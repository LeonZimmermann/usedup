package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.mockProduct
import junit.framework.Assert.assertEquals
import org.junit.Test

class InMemoryQuantityProcessorUnitTest {

  private val testee = InMemoryQuantityProcessor()

  @Test
  fun `given a set of product new quantity pairs, when updateSingleProductQuantity is called, then the products quantity is set to the new quantity`() {
    val product = mockProduct("Product Name", 2.0)
    val updatedQuantity = 2.5

    testee.updateSingleProductQuantity(product, updatedQuantity)

    assertEquals(2.5, product.quantity)
  }

  @Test
  fun `given a product and the new quantity, when updateMultipleProductQuantities is called, then the products quantities are set to the new quantities`() {
    val productOne = mockProduct("Product One", 2.0)
    val productTwo = mockProduct("Product Two", 3.0)
    val data = listOf(
      productOne to 2.5,
      productTwo to 2.0
    )

    testee.updateMultipleProductQuantities(data)

    assertEquals(2.5, productOne.quantity)
    assertEquals(2.0, productTwo.quantity)
  }
}