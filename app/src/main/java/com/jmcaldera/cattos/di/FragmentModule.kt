package com.jmcaldera.cattos.di

import com.jmcaldera.cattos.di.viewmodel.ViewModelModule
import com.jmcaldera.cattos.features.CatsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ViewModelModule::class])
abstract class FragmentModule {

  @ContributesAndroidInjector
  abstract fun contributesCatsFragment(): CatsFragment
}