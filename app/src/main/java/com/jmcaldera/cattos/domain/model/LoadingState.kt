package com.jmcaldera.cattos.domain.model

import com.jmcaldera.cattos.domain.model.Status.ERROR
import com.jmcaldera.cattos.domain.model.Status.LOADING
import com.jmcaldera.cattos.domain.model.Status.SUCCESS

data class LoadingState<out T>(
  val status: Status,
  val data: T?,
  val message: String?
) {
  companion object {
    fun <T> success(data: T?): LoadingState<T> {
      return LoadingState(SUCCESS, data, null)
    }

    fun <T> error(
      msg: String,
      data: T?
    ): LoadingState<T> {
      return LoadingState(ERROR, data, msg)
    }

    fun <T> loading(data: T?): LoadingState<T> {
      return LoadingState(LOADING, data, null)
    }
  }
}