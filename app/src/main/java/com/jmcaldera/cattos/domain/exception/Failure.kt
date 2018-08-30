package com.jmcaldera.cattos.domain.exception

sealed class Failure

class NetworkError : Failure()
data class ResponseUnsuccessful(val error: String?) : Failure()
data class ServerError(val t: Throwable) : Failure()
