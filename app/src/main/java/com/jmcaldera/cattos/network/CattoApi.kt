package com.jmcaldera.cattos.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CattoApi {
  // TODO: use retrofit coroutines adapter

  @GET("v1/images/search?size=small&mime_types=jpg,gif&format=json&order=ASC&limit=10&page=1")
  fun getCatImages(): Call<List<CatResponse>>

  @GET("v1/images/search?size=small&mime_types=jpg,gif&format=json&order=ASC&limit=10")
  fun getCatImages(@Query("page") page: Int): Call<List<CatResponse>>
}