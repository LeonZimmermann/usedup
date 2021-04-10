package de.usedup.android.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.usedup.android.navigation.NavigationStateHolder

@Module
@InstallIn(SingletonComponent::class)
object AppStateModule {

  @Provides
  fun provideUserRepository(): NavigationStateHolder = NavigationStateHolder
}