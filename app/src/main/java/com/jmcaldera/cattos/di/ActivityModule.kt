package com.jmcaldera.cattos.di

import com.jmcaldera.cattos.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [FragmentModule::class])
abstract class ActivityModule {

  @ContributesAndroidInjector
  abstract fun contributesMainActivity(): MainActivity
}