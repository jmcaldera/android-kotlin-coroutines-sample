package com.jmcaldera.cattos.domain.functional

sealed class Either<out L, out R> {
  val isRight get() = this is Right<R>
  val isLeft get() = this is Left<L>

  companion object
}

data class Left<out L>(val error: L) : Either<L, Nothing>()
data class Right<out R>(val value: R) : Either<Nothing, R>()

fun <R> Either.Companion.pure(value: R): Either<Nothing, R> = Right(value)

fun<L, R> Either<L, R>.fold(fnL: (L) -> Any, fnR: (R) -> Any): Any =
  when(this) {
    is Left -> fnL(error)
    is Right -> fnR(value)
  }

fun <T, L, R> Either<L, R>.map(block: (R) -> T): Either<L, T> =
  flatMap { right(block(it)) }

fun <T, L, R> Either<L, R>.flatMap(block: (R) -> Either<L, T>): Either<L, T> =
  when(this) {
    is Left -> Left(error)
    is Right -> block(value)
  }

fun <L> left(error: L) = Left(error)
fun <R> right(value: R) = Right(value)