package com.jmcaldera.cattos.di

import com.jmcaldera.cattos.CattoApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
      AndroidInjectionModule::class,
      AndroidSupportInjectionModule::class,
      ApplicationModule::class,
      NetworkModule::class,
      ActivityModule::class
    ]
)
interface CattoApplicationComponent : AndroidInjector<CattoApplication> {

  @Component.Builder
  abstract class Builder : AndroidInjector.Builder<CattoApplication>()
}