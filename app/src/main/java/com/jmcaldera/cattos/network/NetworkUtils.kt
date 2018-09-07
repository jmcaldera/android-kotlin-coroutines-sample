@file:JvmName("NetworkUtils")
package com.jmcaldera.cattos.network

import com.jmcaldera.cattos.domain.exception.Failure
import com.jmcaldera.cattos.domain.exception.ResponseUnsuccessful
import com.jmcaldera.cattos.domain.exception.ServerError
import com.jmcaldera.cattos.domain.functional.Either
import com.jmcaldera.cattos.domain.functional.left
import com.jmcaldera.cattos.domain.functional.right
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

fun <T> performRequest(call: Call<T>): Either<Failure, Response<T>> {
  return try {
    val response = call.execute()
    return right(response)
  } catch (exception: IOException) {
    left(ServerError(exception))
  }
}

fun <T> Response<T>.toApiResponse(): ApiResponse<T> = ApiResponse.create(this)

fun <T, R> transformResponse(response: ApiResponse<T>, default: T, transform: (T) -> R): Either<Failure, R> {
  return when(response) {
    is ApiSuccessResponse -> right(transform(response.body))
    is ApiEmptyResponse -> right(transform(default))
    is ApiErrorResponse -> left(ResponseUnsuccessful(response.errorMessage))
  }
}

fun <T> ApiResponse<T>.isSuccessful(): Boolean = when(this) {
  is ApiSuccessResponse -> true
  else -> false
}

fun <T> getCattosHeaders(response: ApiSuccessResponse<T>): Map<String, Int> = response.headers
