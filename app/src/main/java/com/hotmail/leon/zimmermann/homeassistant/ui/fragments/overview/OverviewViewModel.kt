package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository

class OverviewViewModel(application: Application) : AndroidViewModel(application) {
    val products: MutableList<Product> = ProductRepository.products
}
