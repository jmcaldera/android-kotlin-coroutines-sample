package com.jmcaldera.cattos.network

import okhttp3.Headers
import retrofit2.Response

sealed class ApiResponse<T> {
  companion object {
    fun <T> create(response: Response<T>): ApiResponse<T> {
      return if (response.isSuccessful) {
        response.body()?.let { ApiSuccessResponse(it, extractHeaders(response.headers())) }
            ?: ApiEmptyResponse()
      } else {
        val msg = response.errorBody()
            ?.string()
        val errorMsg = if (msg.isNullOrEmpty()) {
          response.message()
        } else {
          msg
        }
        ApiErrorResponse(errorMsg ?: "unknown error")
      }
    }

    private fun extractHeaders(headers: Headers?): Map<String, Int> {
      return headers?.let {
        mapOf(
            "count" to it.get("pagination-count")!!.toInt(),
            "page" to it.get("pagination-page")!!.toInt(),
            "limit" to it.get("pagination-limit")!!.toInt()
        )
      } ?: emptyMap()
    }
  }
}

data class ApiSuccessResponse<T>(
  val body: T,
  val headers: Map<String, Int>
) : ApiResponse<T>()

class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()