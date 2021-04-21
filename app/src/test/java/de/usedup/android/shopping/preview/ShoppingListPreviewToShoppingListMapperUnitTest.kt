package de.usedup.android.shopping.preview

import de.usedup.android.datamodel.api.repositories.MeasureRepository
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import de.usedup.android.mockProduct
import de.usedup.android.shopping.data.ShoppingMeal
import de.usedup.android.shopping.data.ShoppingProduct
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingListPreviewToShoppingListMapperUnitTest {

  @MockK
  private lateinit var productRepository: ProductRepository

  @MockK
  private lateinit var measureRepository: MeasureRepository

  @InjectMockKs
  private lateinit var testee: ShoppingListPreviewToShoppingListMapper

  @Before
  fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

  @Test
  fun `given additionalProductList and productDiscrepancyList, when mapPreviewToShoppingList is called, then join both sets into one and add cartAmounts`() =
    runBlockingTest {
      // Given
      val product1 = mockProduct(name = "Product 1", quantity = 0.0, min = 1, max = 1)
      val product2 = mockProduct(name = "Product 2", quantity = 0.0, min = 1, max = 1)
      val product3 = mockProduct(name = "Product 3", quantity = 0.0, min = 1, max = 1)

      val additionalProductList = setOf(
        ShoppingProduct(product1, 2),
        ShoppingProduct(product2, 1))

      val productDiscrepancyList = setOf(
        ShoppingProduct(product1, 4),
        ShoppingProduct(product3, 3),
      )

      val mealList = setOf<ShoppingMeal>()

      val shoppingListPreview = ShoppingListPreview(additionalProductList, productDiscrepancyList, mealList)

      // When
      val result = testee.mapPreviewToShoppingList(shoppingListPreview)

      // Then
      val expectedSet = setOf(
        ShoppingProduct(product1, 6),
        ShoppingProduct(product2, 1),
        ShoppingProduct(product3, 3),
      )

      assertThat(result.shoppingProducts)
        .hasSize(3)
        .containsExactlyInAnyOrderElementsOf(expectedSet)
    }
}