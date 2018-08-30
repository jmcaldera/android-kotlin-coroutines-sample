package com.jmcaldera.cattos.di

import android.content.Context
import com.jmcaldera.cattos.CattoApplication
import dagger.Binds
import dagger.Module

@Module
abstract class ApplicationModule {

  @Binds
  abstract fun bindContext(app: CattoApplication): Context
}