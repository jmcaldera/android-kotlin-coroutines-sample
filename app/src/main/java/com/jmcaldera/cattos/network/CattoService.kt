package com.jmcaldera.cattos.network

import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CattoService
@Inject constructor(retrofit: Retrofit): CattoApi {

  private val cattoApi by lazy { retrofit.create(CattoApi::class.java) }

  override fun getCatImages(): Call<List<CatResponse>> {
    return cattoApi.getCatImages()
  }

  override fun getCatImages(page: Int): Call<List<CatResponse>> {
    return cattoApi.getCatImages(page)
  }
}