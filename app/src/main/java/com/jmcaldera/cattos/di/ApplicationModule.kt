package com.jmcaldera.cattos.di

import android.content.Context
import com.jmcaldera.cattos.CattoApplication
import com.jmcaldera.cattos.domain.AppDispatchers
import com.jmcaldera.cattos.domain.Dispatchers
import dagger.Binds
import dagger.Module

@Module
abstract class ApplicationModule {

  @Binds
  abstract fun bindContext(app: CattoApplication): Context

  @Binds
  abstract fun bindDispatchers(dispatchers: AppDispatchers): Dispatchers
}