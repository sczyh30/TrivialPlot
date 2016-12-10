package io.sczyh30.tplot.util

import io.sczyh30.tplot.lexer._
import io.sczyh30.tplot.parser._

/**
  * Implicit converter.
  * Typeclass Pattern.
  */
object Show {

  implicit class TokenCov(tk: Token) {
    def string: String = tk match {
      case ATOM(x) => x
      case NUMBER(x) => x.toString
      case ADD => "+"
      case SUB => "-"
      case MUL => "*"
      case DIV => "/"
      case LP => "("
      case RP => ")"
      case COMMA => ","
      case SEMICOLON => ";"
      case _ => tk.toString
    }
  }

  implicit class ASTCov(node: ASTNode) {
    def string: String = node match {
      case TranslationUnit(l) =>
        l.map(_.string).mkString("\n")
      case other@_ => other.toString
    }
  }

}
