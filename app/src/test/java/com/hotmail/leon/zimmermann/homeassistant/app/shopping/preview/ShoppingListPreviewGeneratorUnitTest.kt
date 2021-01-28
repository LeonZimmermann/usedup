package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.PlannerRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingListPreviewGeneratorUnitTest {

  @MockK
  private lateinit var productRepository: ProductRepository

  @MockK
  private lateinit var mealRepository: MealRepository

  @MockK
  private lateinit var plannerRepository: PlannerRepository

  @InjectMockKs
  private lateinit var testee: ShoppingListPreviewGenerator

  @Before
  fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

  @Test
  fun `given any repository state, when generateShoppingListPreview() is called, then ShoppingListPreview should have an empty set as additionalProducts`() =
    runBlockingTest {
      // Given
      every { productRepository.getAllProducts() } returns listOf()
      every { plannerRepository.getAllPlannerItems() } returns listOf()
      // When
      val result = testee.generateShoppingListPreview()
      // Then
      assertTrue(result.additionalProductList.isEmpty())
    }

  @Test
  fun `given four products in the repository with one being below minimum, when generateShoppingListPreview() is called, then ShoppingListPreview should have this product in the productDisrepancy set`() =
    runBlockingTest {
      // Given
      every { productRepository.getAllProducts() } returns listOf(
        mockProduct("Product One", 2.0, 3, 2),
        mockProduct("Product Two", 3.0, 3, 2),
        mockProduct("Product Three", 4.0, 3, 2),
        mockProduct("Product Four", 5.0, 3, 2)
      )
      every { plannerRepository.getAllPlannerItems() } returns listOf()
      // When
      val result = testee.generateShoppingListPreview()
      // Then
      assertEquals(1, result.productDiscrepancyList.size)
      assertEquals(1, result.productDiscrepancyList.first().cartAmount)
    }

  @Test
  fun test() {
    every { productRepository.getAllProducts() } returns listOf(mockProduct("Product One", 1.0, 1, 2))
  }

  fun mockProduct(name: String = "", quantity: Double = 0.0, min: Int = 0, max: Int = 0,
    capacity: Double = 0.0): Product =
    Product(mockk(), name, quantity, min, max, capacity, mockk(), mockk())
}