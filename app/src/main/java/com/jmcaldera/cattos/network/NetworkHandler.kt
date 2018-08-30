package com.jmcaldera.cattos.network

import android.content.Context
import com.jmcaldera.cattos.util.networkInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHandler
@Inject constructor(private val context: Context) {
  val isConnected get() = context.networkInfo?.isConnected
}