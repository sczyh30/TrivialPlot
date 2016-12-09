package io.sczyh30.tplot.parser

import io.sczyh30.tplot.lexer.Token
import io.sczyh30.tplot.lexer.TokenConverter.ImpCov
import io.sczyh30.tplot.parser.Parser.{Bad, Result}

import scala.util.{Failure, Success}

/**
  * Parse stream type class.
  *
  * @author Eric Zhao
  */
object ParseStream {
  implicit class ParseStreamTypeclass[T](cur: Result[T]) { // TODO: Expand to all monads.

    def ~~>[In, Out](in: Result[In]): Result[Out] = {
      ???
    }

    /**
      * Match the provided token.
      * If matched then continue, else fail.
      * Should only match case objects!
      *
      * @param tk pending match token
      * @return result
      */
    def @~>(tk: Token): Result[T] = cur match {
      case Success((t, c :: rs)) =>
        if (c.eq(tk)) Success(t, rs)
        else Bad(s"Expected token ${tk.string}, but found ${c.string}")
      case failure@Failure(_) => failure
      case _ => Bad("Unexpected error when parsing with @~>")
    }

    def lift[M](f: T => M): Result[M] = cur map {
      case (x, rs) => (f(x), rs)
    }

    def ~>>[R](f: T => Result[R]): Result[R] = cur match {
      case _ => ???
    }
  }
}
