package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Before

class ShoppingListPreviewToShoppingListMapperUnitTest {

  @MockK
  private lateinit var productRepository: ProductRepository

  @MockK
  private lateinit var measureRepository: MeasureRepository

  @InjectMockKs
  private lateinit var testee: ShoppingListPreviewToShoppingListMapper

  @Before
  fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

}