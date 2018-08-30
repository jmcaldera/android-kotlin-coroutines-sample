package com.jmcaldera.cattos.domain

data class Cat(
  val id: String,
  val url: String
) {
  companion object {
    fun empty() = Cat("EMPTY", "EMPTY")
  }
}