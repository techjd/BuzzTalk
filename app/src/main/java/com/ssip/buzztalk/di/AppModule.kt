package com.ssip.buzztalk.di

import com.ssip.buzztalk.ui.components.HomeBottomSheet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import javax.inject.Singleton

@InstallIn(FragmentComponent::class)
@Module
object AppModule {

  @Provides
  fun providesHomeBottomSheet(): HomeBottomSheet {
    return HomeBottomSheet()
  }
}