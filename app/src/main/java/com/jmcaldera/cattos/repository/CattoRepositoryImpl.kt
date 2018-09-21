package com.jmcaldera.cattos.repository

import com.jmcaldera.cattos.domain.model.Cat
import com.jmcaldera.cattos.domain.CattoRepository
import com.jmcaldera.cattos.domain.CommonDispatchers
import com.jmcaldera.cattos.domain.exception.Failure
import com.jmcaldera.cattos.domain.exception.NetworkError
import com.jmcaldera.cattos.domain.functional.Either
import com.jmcaldera.cattos.domain.functional.flatMap
import com.jmcaldera.cattos.domain.functional.left
import com.jmcaldera.cattos.domain.functional.map
import com.jmcaldera.cattos.network.ApiSuccessResponse
import com.jmcaldera.cattos.network.CattoService
import com.jmcaldera.cattos.network.NetworkHandler
import com.jmcaldera.cattos.network.getCattosHeaders
import com.jmcaldera.cattos.network.isSuccessful
import com.jmcaldera.cattos.network.performRequest
import com.jmcaldera.cattos.network.toApiResponse
import com.jmcaldera.cattos.network.transformResponse
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CattoRepositoryImpl
@Inject constructor(
  private val appDispatchers: CommonDispatchers,
  private val networkHandler: NetworkHandler,
  private val cattoService: CattoService
) : CattoRepository {

  private var cattoHeaders: Map<String, Int> = emptyMap()

  private var nextPage: Int = 0
    get() {
      return if (cattoHeaders.isEmpty()) {
        1
      } else {
        var currentPage: Int = cattoHeaders["page"]!!
        val hasMorePages = cattoHeaders["count"]!! > currentPage * cattoHeaders["limit"]!!
        if (hasMorePages) ++currentPage else 0  // TODO what happens when I reach last page?

      }
    }

  override suspend fun getCats(): Either<Failure, List<Cat>> {

    // Do something in diskIO thread before executing background request
    withContext(appDispatchers.diskIO) {
      val result = 2 * 2 + 5
      println("Result: $result, Calculated in thread: ${Thread.currentThread().name}")
      delay(2000)
      println("After delay: ${Thread.currentThread().name}")
    }
    println("Retrofit thread: ${Thread.currentThread().name}")

    return when (networkHandler.isConnected) {
      true -> performRequest(cattoService.getCatImages())
          .map { response -> response.toApiResponse() }
          .flatMap { apiResponse ->
            if (apiResponse.isSuccessful()) {
              cattoHeaders = getCattosHeaders(apiResponse as ApiSuccessResponse)
              println("Headers: $cattoHeaders")
            }
            return@flatMap transformResponse(
                apiResponse, emptyList()
            ) { cats -> cats.map { it.toDomain() } }
          }

      false, null -> left(NetworkError())
    }
  }

  override suspend fun getCatsNextPage(): Either<Failure, List<Cat>> {
    return when (networkHandler.isConnected) {
      true -> performRequest(cattoService.getCatImages(nextPage))
          .map { response -> response.toApiResponse() }
          .flatMap { apiResponse ->
            if (apiResponse.isSuccessful()) {
              cattoHeaders = getCattosHeaders(apiResponse as ApiSuccessResponse)
              println("Headers: $cattoHeaders")
            }
            return@flatMap transformResponse(
                apiResponse, emptyList()
            ) { cats -> cats.map { it.toDomain() } }
          }

      false, null -> left(NetworkError())
    }
  }

}