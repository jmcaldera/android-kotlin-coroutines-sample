package com.jmcaldera.cattos.common

import com.jmcaldera.cattos.domain.Dispatchers
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Unconfined

class TestDispatcher(private val context: CoroutineDispatcher = Unconfined) : Dispatchers {

  override val diskIO: CoroutineDispatcher
    get() = context

  override val network: CoroutineDispatcher
    get() = context

  override val main: CoroutineDispatcher
    get() = context
}