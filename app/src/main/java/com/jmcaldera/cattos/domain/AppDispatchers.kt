package com.jmcaldera.cattos.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

interface CommonDispatchers {
  val diskIO: CoroutineDispatcher
  val background: CoroutineDispatcher
  val main: CoroutineDispatcher
}

@Singleton
open class AppDispatchers
@Inject constructor() : CommonDispatchers {

  override val diskIO: CoroutineDispatcher = Dispatchers.IO
  override val background: CoroutineDispatcher = Dispatchers.Default
  override val main: CoroutineDispatcher = Dispatchers.Main
}