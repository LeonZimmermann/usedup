package com.hotmail.leon.zimmermann.homeassistant.app

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.product.ProductRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories.product.FirebaseProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object DataModelModule {

  @Provides
  fun provideUserRepository(): UserRepository = FirebaseUserRepository

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