package com.jmcaldera.cattos.domain

import com.jmcaldera.cattos.domain.exception.Failure
import com.jmcaldera.cattos.domain.functional.Either
import com.jmcaldera.cattos.domain.model.Cat

interface CattoRepository {

  suspend fun getCats(): Either<Failure, List<Cat>>

  suspend fun getCatsNextPage(): Either<Failure, List<Cat>>
}