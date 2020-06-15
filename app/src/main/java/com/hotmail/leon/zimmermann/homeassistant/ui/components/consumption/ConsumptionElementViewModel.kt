package com.hotmail.leon.zimmermann.homeassistant.ui.components.consumption

import androidx.lifecycle.ViewModel
import com.hotmail.leon.zimmermann.homeassistant.datamodel.Measure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.ProductRepository

class ConsumptionElementViewModel: ViewModel() {
    val products: List<Pair<String, Product>> = ProductRepository.products
    val measures: MutableList<Pair<String, Measure>> = MeasureRepository.measures
}