package com.jmcaldera.cattos.domain.model

data class Cat(
  val id: String,
  val url: String
) {
  companion object {
    fun empty() = Cat("EMPTY", "EMPTY")
  }
}