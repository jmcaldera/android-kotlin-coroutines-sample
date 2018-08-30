package com.jmcaldera.cattos.domain

import com.jmcaldera.cattos.domain.exception.Failure
import com.jmcaldera.cattos.domain.functional.Either

interface CattoRepository {

  suspend fun getCatImages(): Either<Failure, List<Cat>>
}