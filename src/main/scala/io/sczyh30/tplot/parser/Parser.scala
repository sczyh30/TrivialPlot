package io.sczyh30.tplot.parser

import io.sczyh30.tplot.lexer.Token

/**
  * TrivialPlot parser class.
  *
  * @author Eric Zhao
  */
class Parser {

  type ParseError = (Token, String)

  def parse(tokens: List[Token]): Either[ParseError, Stmt] = {
    ???
  }

}
