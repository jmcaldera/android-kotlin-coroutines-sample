package com.jmcaldera.cattos

import com.jmcaldera.cattos.di.DaggerCattoApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class CattoApplication : DaggerApplication() {

  override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
    DaggerCattoApplicationComponent.builder().create(this)

}