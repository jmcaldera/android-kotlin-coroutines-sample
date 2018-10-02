package com.jmcaldera.cattos.repository

import com.jmcaldera.cattos.common.TestDispatcher
import com.jmcaldera.cattos.common.UnitTest
import com.jmcaldera.cattos.domain.CommonDispatchers
import com.jmcaldera.cattos.domain.exception.NetworkError
import com.jmcaldera.cattos.domain.functional.fold
import com.jmcaldera.cattos.domain.functional.right
import com.jmcaldera.cattos.domain.model.Cat
import com.jmcaldera.cattos.network.CatResponse
import com.jmcaldera.cattos.network.CattoService
import com.jmcaldera.cattos.network.NetworkHandler
import kotlinx.coroutines.experimental.runBlocking
import okhttp3.Headers
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions
import retrofit2.Call
import retrofit2.Response
import org.mockito.Mockito.`when` as given

class CattoRepositoryImplTest : UnitTest() {

  private lateinit var repository: CattoRepositoryImpl
  private lateinit var dispatchers: CommonDispatchers
  @Mock private lateinit var networkHandler: NetworkHandler
  @Mock private lateinit var service: CattoService
  @Mock private lateinit var getCatsCall: Call<List<CatResponse>>

  @Before
  fun setup() {
    dispatchers = TestDispatcher()
    repository = CattoRepositoryImpl(dispatchers, networkHandler, service)
  }

  @Test
  fun `get cats should call service`() = runBlocking<Unit> {
    val mockResponse: Response<List<CatResponse>> = Response.success(null)

    given(networkHandler.isConnected).thenReturn(true)
    given(getCatsCall.execute()).thenReturn(mockResponse)
    given(service.getCatImages()).thenReturn(getCatsCall)

    repository.getCats()
    verify(service, times(1)).getCatImages()
  }

  @Test
  fun `get cats should return empty list by default`() = runBlocking<Unit> {
    val mockResponse: Response<List<CatResponse>> = Response.success(null)
    given(networkHandler.isConnected).thenReturn(true)
    given(getCatsCall.execute()).thenReturn(mockResponse)
    given(service.getCatImages()).thenReturn(getCatsCall)

    val cats = repository.getCats()
    assertTrue(cats.isRight)
    assertTrue(cats == right(emptyList<List<Cat>>()))
    verify(service, times(1)).getCatImages()
  }

  @Test
  fun `get cats should return cat list`() = runBlocking<Unit> {
    val mockResponse: Response<List<CatResponse>> = Response.success(
        listOf(CatResponse("id", "url")), getHeaders()
    )
    given(networkHandler.isConnected).thenReturn(true)
    given(getCatsCall.execute()).thenReturn(mockResponse)
    given(service.getCatImages()).thenReturn(getCatsCall)

    val cats = repository.getCats()
    assertTrue(cats.isRight)
    assertTrue(cats == right(listOf(Cat("id", "url"))))
    verify(service, times(1)).getCatImages()
  }

  @Test
  fun `get cats should return network error when no connection`() = runBlocking<Unit> {

    given(networkHandler.isConnected).thenReturn(false)

    val cats = repository.getCats()
    assertTrue(cats.isLeft)
    cats.fold({ failure -> assertTrue(failure is NetworkError) }, {})
    verifyZeroInteractions(service)
  }

  private fun getHeaders(): Headers = Headers.of(
      mapOf(
              "pagination-count" to "1000",
              "pagination-page" to "1",
              "pagination-limit" to "10"
      )
  )
}