package com.jmcaldera.cattos.network

import com.jmcaldera.cattos.domain.Cat
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CatResponse(
  val id: String,
  val url: String
) {
  fun toDomain() = Cat(id, url)
}