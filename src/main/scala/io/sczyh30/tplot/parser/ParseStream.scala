package io.sczyh30.tplot.parser

import io.sczyh30.tplot.lexer.Token
import io.sczyh30.tplot.lexer.TokenConverter.ImpCov
import io.sczyh30.tplot.parser.Parser.{Bad, Result}

import scala.util.{Failure, Success}

/**
  * Parse stream type classes.
  *
  * @author Eric Zhao
  */
object ParseStream {

  // TODO: Expand to all monads.

  implicit class ParseStreamTypeclass[T](cur: Result[T]) {

    /**
      * Match the provided token.
      * If matched then continue, else fail.
      * Should only match case objects!
      *
      * @param tk pending match token
      * @return result
      */
    def |@~>(tk: Token): Result[T] = cur match {
      case Success((t, c :: rs)) =>
        if (c.eq(tk)) Success(t, rs)
        else Bad(s"Expected token ${tk.string}, but found ${c.string}")
      case failure@Failure(_) => failure
      case _ => Bad("Unexpected error when parsing with |@~> (token-match-strict)")
    }

    /**
      * This is equivalent to `fmap`(<$>) in Haskell for `x` in `Result`.
      */
    def <#>[R](f: T => R): Result[R] = cur map {
      case (x, rs) => (f(x), rs)
    }
  }

  implicit class ParseTokenTypeclass[T](tk: Token) {

    /**
      * Match the provided token.
      * If matched then continue, else fail.
      * Should only match case objects!
      *
      * @return result
      */
    def @|~>(r: Result[T]): Result[T] = r match {
      case Success((t, c :: rs)) =>
        if (c.eq(tk)) Success(t, rs)
        else Bad(s"Expected token ${tk.string}, but found ${c.string}")
      case failure@Failure(_) => failure
      case _ => Bad("Unexpected error when parsing with @|~> (token-match-strict)")
    }
  }

}
