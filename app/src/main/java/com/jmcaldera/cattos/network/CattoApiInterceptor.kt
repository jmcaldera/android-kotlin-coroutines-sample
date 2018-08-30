package com.jmcaldera.cattos.network

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class CattoApiInterceptor(private val apiKey: String): Interceptor {

  override fun intercept(chain: Chain?): Response {
    val request = chain?.request()

    val newRequest = request?.newBuilder()!!
        .header("x-api-key", apiKey)
        .build()

    return chain.proceed(newRequest)
  }
}