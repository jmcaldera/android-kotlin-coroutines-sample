package com.jmcaldera.cattos.repository

import com.jmcaldera.cattos.domain.Cat
import com.jmcaldera.cattos.domain.CattoRepository
import com.jmcaldera.cattos.domain.Dispatchers
import com.jmcaldera.cattos.domain.exception.Failure
import com.jmcaldera.cattos.domain.exception.NetworkError
import com.jmcaldera.cattos.domain.exception.ResponseUnsuccessful
import com.jmcaldera.cattos.domain.exception.ServerError
import com.jmcaldera.cattos.domain.functional.Either
import com.jmcaldera.cattos.domain.functional.left
import com.jmcaldera.cattos.domain.functional.right
import com.jmcaldera.cattos.network.CattoService
import com.jmcaldera.cattos.network.NetworkHandler
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.withContext
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CattoRepositoryImpl
@Inject constructor(
  private val appDispatchers: Dispatchers,
  private val networkHandler: NetworkHandler,
  private val cattoService: CattoService
) : CattoRepository {

  override suspend fun getCatImages(): Either<Failure, List<Cat>> {

    // Do something in diskIO thread before executing network request
    withContext(appDispatchers.diskIO) {
      val result = 2 * 2 + 5
      println("Result: $result, Calculated in thread: ${Thread.currentThread().name}")
      delay(2000)
      println("After delay: ${Thread.currentThread().name}")
    }
    println("Retrofit thread: ${Thread.currentThread().name}")

    return when (networkHandler.isConnected) {
      true -> request(cattoService.getCatImages(), { it.map { it.toDomain() } }, emptyList())
      false, null -> left(NetworkError())
    }
  }

  private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
    return try {
      val response = call.execute()
      when (response.isSuccessful) {
        true -> right(transform((response.body() ?: default)))
        false -> left(ResponseUnsuccessful(response.errorBody()?.string()))
      }
    } catch (exception: Throwable) {
      left(ServerError(exception))
    }
  }

}