package com.hotmail.leon.zimmermann.homeassistant.components.consumption

import androidx.lifecycle.ViewModel
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Measure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository

class ConsumptionElementViewModel: ViewModel() {
    val products: List<Product> = ProductRepository.products
    val measures: MutableList<Measure> = MeasureRepository.measures
}