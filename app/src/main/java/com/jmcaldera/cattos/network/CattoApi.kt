package com.jmcaldera.cattos.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CattoApi {

  @GET("v1/images/search?size=med&mime_types=jpg,gif&format=json&order=RANDOM&page=0")
  fun getCatImages(@Query("limit") limit: Int = 10): Call<List<CatResponse>>
}