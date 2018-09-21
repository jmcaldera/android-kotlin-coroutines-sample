package com.jmcaldera.cattos.domain

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.newSingleThreadContext
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

  override val diskIO: CoroutineDispatcher = newSingleThreadContext("diskThread")
  override val background: CoroutineDispatcher = Dispatchers.Default
  override val main: CoroutineDispatcher = Dispatchers.Main
}