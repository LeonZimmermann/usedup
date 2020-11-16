package com.hotmail.leon.zimmermann.homeassistant.app.shopping.list

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingProduct
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class ShoppingViewModel @ViewModelInject constructor() : ViewModel() {
}
