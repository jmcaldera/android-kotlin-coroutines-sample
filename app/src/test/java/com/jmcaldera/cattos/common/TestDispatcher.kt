package com.jmcaldera.cattos.common

import com.jmcaldera.cattos.domain.CommonDispatchers
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers

class TestDispatcher(private val context: CoroutineDispatcher = Dispatchers.Unconfined) : CommonDispatchers {

  override val diskIO: CoroutineDispatcher
    get() = context

  override val background: CoroutineDispatcher
    get() = context

  override val main: CoroutineDispatcher
    get() = context
}