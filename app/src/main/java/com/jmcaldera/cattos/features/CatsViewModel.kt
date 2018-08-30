package com.jmcaldera.cattos.features

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import com.jmcaldera.cattos.domain.Cat
import com.jmcaldera.cattos.domain.CattoRepository
import com.jmcaldera.cattos.domain.exception.Failure
import com.jmcaldera.cattos.domain.functional.fold
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

class CatsViewModel
@Inject constructor(private val cattoRepository: CattoRepository) : ViewModel() {

  private val rootJob = Job()

  private val _cats: MutableLiveData<List<Cat>> = MutableLiveData()

  val cats: MediatorLiveData<List<Cat>> = MediatorLiveData()

  val error: MutableLiveData<Boolean> = MutableLiveData()

  init {
    cats.addSource(_cats) { newCats -> cats.value = newCats }
  }

  fun getCats() {
    launch(parent = rootJob) {
      val result = withContext(kotlin.coroutines.experimental.coroutineContext) {
        cattoRepository.getCatImages()
      }

//      val result = async { cattoRepository.getCatImages() }.await()

      println(result)

      withContext(UI) {
        result.fold({ error -> handleError(error) }, { cats -> handleCattos(cats) })
      }
    }
  }

  private fun handleError(failure: Failure) {
    error.value = true
  }

  private fun handleCattos(cats: List<Cat>) {
    println(cats)
    this._cats.value = cats
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun onDestroy() {
    rootJob.cancel()
  }

}