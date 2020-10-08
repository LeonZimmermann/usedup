package com.hotmail.leon.zimmermann.homeassistant.app

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.CategoryRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.MeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.TemplateRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories.FirebaseCategoryRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories.FirebaseMealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories.FirebaseMeasureRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories.FirebaseTemplateRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories.product.FirebaseProductRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ActivityComponent::class)
object DataModelModule {

  @Provides
  fun provideCategoryRepository(): CategoryRepository = FirebaseCategoryRepository

  @Provides
  fun provideMeasureRepository(): MeasureRepository = FirebaseMeasureRepository

  @Provides
  fun provideProductRepository(): ProductRepository = FirebaseProductRepository

  @Provides
  fun provideTemplateRepository(): TemplateRepository = FirebaseTemplateRepository

  @Provides
  fun provideMealRepository(): MealRepository = FirebaseMealRepository

}