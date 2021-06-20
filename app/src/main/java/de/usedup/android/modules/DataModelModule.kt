package de.usedup.android.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.usedup.android.datamodel.api.repositories.*
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import de.usedup.android.datamodel.firebase.repositories.*
import de.usedup.android.datamodel.firebase.repositories.product.FirebaseProductRepository

@Module
@InstallIn(SingletonComponent::class)
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

  @Provides
  fun providePlannerRepository(): PlannerRepository = FirebasePlannerRepository

  @Provides
  fun provideHouseholdRepository(): HouseholdRepository = FirebaseHouseholdRepository

  @Provides
  fun provideImageRepository(): ImageRepository = FirebaseImageRepository

  @Provides
  fun provideInvitationRepository(): InvitationRepository = FirebaseInvitationRepository
}