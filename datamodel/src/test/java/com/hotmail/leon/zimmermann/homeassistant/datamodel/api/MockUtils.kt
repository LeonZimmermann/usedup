package com.hotmail.leon.zimmermann.homeassistant.datamodel.api

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Product
import io.mockk.mockk

fun mockProduct(name: String = "", quantity: Double = 0.0, min: Int = 0, max: Int = 0, capacity: Double = 0.0): Product =
  Product(mockk(), name, quantity, min, max, capacity, mockk(), mockk())