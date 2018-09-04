package com.jmcaldera.cattos.di

import android.content.Context
import com.jmcaldera.cattos.R
import com.jmcaldera.cattos.di.qualifier.ApiKey
import com.jmcaldera.cattos.di.viewmodel.ViewModelModule
import com.jmcaldera.cattos.domain.CattoRepository
import com.jmcaldera.cattos.network.CattoApiInterceptor
import com.jmcaldera.cattos.repository.CattoRepositoryImpl
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class NetworkModule {

  @Provides
  @ApiKey
  fun provideApiKey(context: Context): String =
    context.getString(R.string.catto_api_key)

  @Provides
  @Singleton
  fun provideNetworkCache(context: Context): Cache =
    Cache(context.cacheDir, 10 * 1024 * 1024)   // 10 MB

  @Singleton
  @Provides
  fun provideOkClient(@ApiKey apiKey: String, cache: Cache): OkHttpClient =
    OkHttpClient.Builder()
        .cache(cache)
        .connectTimeout(30, SECONDS)
        .readTimeout(30, SECONDS)
        .writeTimeout(30, SECONDS)
        .addInterceptor(CattoApiInterceptor(apiKey))
        .addInterceptor(HttpLoggingInterceptor().apply {
          level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

  @Singleton
  @Provides
  fun provideRetrofit(okClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .client(okClient)
        .baseUrl("https://api.thecatapi.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()


  @Provides
  @Singleton
  fun provideCattoRepository(repositoryImpl: CattoRepositoryImpl): CattoRepository = repositoryImpl
}