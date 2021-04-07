package de.usedup.android.shopping.preview

import de.usedup.android.datamodel.api.objects.PlannerItem
import de.usedup.android.datamodel.api.objects.Product
import de.usedup.android.datamodel.api.repositories.MealRepository
import de.usedup.android.datamodel.api.repositories.PlannerRepository
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import de.usedup.android.mockProduct
import io.mockk.MockKAnnotations
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
  private lateinit var mealRepository: MealRepository

  @InjectMockKs
  private lateinit var testee: ShoppingListPreviewGenerator

  @Before
  fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

  @Test
  fun `given any repository state, when generateShoppingListPreview() is called, then ShoppingListPreview should have an empty set as additionalProducts`() =
    runBlockingTest {
      // Given
      val products = setOf<Product>()
      val plannerItems = listOf<PlannerItem>()
      // When
      val result = testee.generateShoppingListPreview(products, plannerItems)
      // Then
      assertTrue(result.additionalProductList.isEmpty())
    }

  @Test
  fun `given four products in the repository with one being below minimum, when generateShoppingListPreview() is called, then ShoppingListPreview should have this product in the productDisrepancy set`() =
    runBlockingTest {
      // Given
      val products = setOf(
        mockProduct("Product One", 2.0, 3, 2),
        mockProduct("Product Two", 3.0, 3, 2),
        mockProduct("Product Three", 4.0, 3, 2),
        mockProduct("Product Four", 5.0, 3, 2)
      )
      val plannerItems = listOf<PlannerItem>()
      // When
      val result = testee.generateShoppingListPreview(products, plannerItems)
      // Then
      assertEquals(1, result.productDiscrepancyList.size)
      assertEquals(1, result.productDiscrepancyList.first().cartAmount)
    }

}