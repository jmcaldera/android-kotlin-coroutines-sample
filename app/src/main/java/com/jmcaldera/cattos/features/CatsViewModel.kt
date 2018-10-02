package com.jmcaldera.cattos.features

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jmcaldera.cattos.domain.model.Cat
import com.jmcaldera.cattos.domain.CattoRepository
import com.jmcaldera.cattos.domain.CommonDispatchers
import com.jmcaldera.cattos.domain.exception.Failure
import com.jmcaldera.cattos.domain.exception.NetworkError
import com.jmcaldera.cattos.domain.exception.ResponseUnsuccessful
import com.jmcaldera.cattos.domain.exception.ServerError
import com.jmcaldera.cattos.domain.functional.fold
import com.jmcaldera.cattos.domain.model.LoadingState
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import kotlin.coroutines.experimental.CoroutineContext

class CatsViewModel
@Inject constructor(
  private val appDispatchers: CommonDispatchers,
  private val cattoRepository: CattoRepository
) : ViewModel(), CoroutineScope {

  override val coroutineContext: CoroutineContext
    get() = appDispatchers.background + rootJob // Combine to cancel from rootJob reference

  private var rootJob: Job = Job()

  private val _cats: MutableLiveData<List<Cat>> = MutableLiveData()

  val cats: MediatorLiveData<List<Cat>> = MediatorLiveData()

  val loadingState: MutableLiveData<LoadingState<List<Cat>>> = MutableLiveData()

  val loadMoreState: MutableLiveData<LoadMoreState> = MutableLiveData()

  init {
    cats.addSource(_cats) { newCats -> cats.value = newCats }
    loadMoreState.value = LoadMoreState(false, null)
  }

  fun getCats() {

    loadingState.value = LoadingState.loading(null)
    launch {
      val result = cattoRepository.getCats()

      println(result)

      withContext(appDispatchers.main) {
        println("UI thread: ${Thread.currentThread().name}")
        result.fold(
            { error -> handleError(error) },
            { cats -> handleCattos(cats) }
        )
      }
    }
  }

  fun loadNextPage() {
    loadMoreState.value = LoadMoreState(true, null)
    launch {
      val result = cattoRepository.getCatsNextPage()

      println(result)

      withContext(appDispatchers.main) {
        println("UI thread: ${Thread.currentThread().name}")
        result.fold(
            { error -> handleErrorNextPage(error) },
            { cats -> handleCattosNextPage(cats) }
        )
      }
    }
  }

  private fun handleError(failure: Failure) {
    loadingState.value = LoadingState.error(
        when (failure) {
          is NetworkError -> "Network Error"
          is ResponseUnsuccessful -> failure.error ?: "Unsuccessful Response"
          is ServerError -> failure.t.message ?: "Server Error"
        }, null
    )
  }

  private fun handleErrorNextPage(failure: Failure) {
    loadMoreState.value = LoadMoreState(
        false, when (failure) {
      is NetworkError -> "Network Error"
      is ResponseUnsuccessful -> failure.error ?: "Unsuccessful Response"
      is ServerError -> failure.t.message
    }
    )
  }

  private fun handleCattos(cats: List<Cat>) {
    loadingState.value = LoadingState.success(cats)
    this._cats.value = cats
  }

  private fun handleCattosNextPage(cats: List<Cat>) {
    loadMoreState.value = LoadMoreState(false, null)
    val previousList = _cats.value as MutableList?
    previousList?.addAll(cats)
    this._cats.value = previousList
  }

  fun stopCoroutines() {
    val cancelled = rootJob.cancel()
    if (cancelled) {
      println("Coroutines cancelled")
    } else {
      println("Coroutines not cancelled")
    }
    println("Destroyed")
  }

  override fun onCleared() {
    super.onCleared()
    stopCoroutines()
  }

  class LoadMoreState(
    val isRunning: Boolean,
    val errorMessage: String?
  ) {
    private var handledError = false

    val errorMessageIfNotHandled: String?
      get() {
        if (handledError) {
          return null
        }
        handledError = true
        return errorMessage
      }
  }

}