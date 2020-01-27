package com.jmcaldera.cattos.di

import androidx.appcompat.app.AppCompatActivity
import com.jmcaldera.cattos.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [FragmentModule::class])
abstract class ActivityModule {

  @ContributesAndroidInjector
  abstract fun contributesMainActivity(): MainActivity

  @Binds
  abstract fun bindsMainActivity(activity: MainActivity): AppCompatActivity
}