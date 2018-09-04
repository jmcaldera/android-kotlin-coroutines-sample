package com.jmcaldera.cattos.features

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jmcaldera.cattos.domain.Cat
import com.jmcaldera.cattos.domain.CattoRepository
import com.jmcaldera.cattos.domain.Dispatchers
import com.jmcaldera.cattos.domain.exception.Failure
import com.jmcaldera.cattos.domain.functional.fold
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

class CatsViewModel
@Inject constructor(
  private val appDispatchers: Dispatchers,
  private val cattoRepository: CattoRepository
) : ViewModel() {

  private val rootJob = Job()

  private val _cats: MutableLiveData<List<Cat>> = MutableLiveData()

  val cats: MediatorLiveData<List<Cat>> = MediatorLiveData()

  val error: MutableLiveData<Boolean> = MutableLiveData()

  init {
    cats.addSource(_cats) { newCats -> cats.value = newCats }
  }

  fun getCats() {
    launch(appDispatchers.network, parent = rootJob) {
      val result = cattoRepository.getCatImages()

      println(result)

      withContext(appDispatchers.main) {
        println("UI thread: ${Thread.currentThread().name}")
        result.fold({ error -> handleError(error) }, { cats -> handleCattos(cats) })
      }
    }
  }

  private fun handleError(failure: Failure) {
    error.value = true
  }

  private fun handleCattos(cats: List<Cat>) {
    this._cats.value = cats
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

}