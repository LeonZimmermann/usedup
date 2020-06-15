package com.hotmail.leon.zimmermann.homeassistant.ui.components.consumption

import androidx.lifecycle.ViewModel
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Measure
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.ProductRepository

class ConsumptionElementViewModel: ViewModel() {
    val products: List<Pair<String, Product>> = ProductRepository.products
    val measures: MutableList<Pair<String, Measure>> = MeasureRepository.measures
}