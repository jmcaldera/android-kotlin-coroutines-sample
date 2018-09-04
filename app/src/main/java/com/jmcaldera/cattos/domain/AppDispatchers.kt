package com.jmcaldera.cattos.domain

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.newSingleThreadContext
import javax.inject.Inject
import javax.inject.Singleton

interface Dispatchers {
  val diskIO: CoroutineDispatcher
  val network: CoroutineDispatcher
  val main: CoroutineDispatcher
}

@Singleton
open class AppDispatchers
@Inject constructor() : Dispatchers {

  override val diskIO: CoroutineDispatcher = newSingleThreadContext("diskThread")
  override val network: CoroutineDispatcher = DefaultDispatcher
  override val main: CoroutineDispatcher = UI
}