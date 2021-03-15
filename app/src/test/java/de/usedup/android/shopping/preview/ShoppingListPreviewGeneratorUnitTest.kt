package de.usedup.android.shopping.preview

import de.usedup.android.app.mockProduct
import com.usedup.android.zimmermann.homeassistant.datamodel.api.repositories.MealRepository
import com.usedup.android.zimmermann.homeassistant.datamodel.api.repositories.PlannerRepository
import com.usedup.android.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingListPreviewGeneratorUnitTest {

  @MockK
  private lateinit var productRepository: ProductRepository

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

}