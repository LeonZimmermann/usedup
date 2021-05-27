package de.usedup.android.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppStateModule {

  @Provides
  fun provideNavigationStateHolder(): NavigationStateHolder = NavigationStateHolder

  @Provides
  fun provideConnectionStateHolder(): ConnectionStateHolder = ConnectionStateHolder
}