package io.sczyh30.tplot.lexer

/**
  * Implicit conversion for tokens.
  * Typeclass Pattern.
  */
object TokenConverter {
  implicit class ImpCov(tk: Token) {
    def string: String = tk match {
      case ATOM(x) => x
      case NUMBER(x) => x.toString
      case _ => tk.toString // TODO
    }
  }
}
