package io.sczyh30.tplot.parser

import io.sczyh30.tplot.lexer.Token
import io.sczyh30.tplot.lexer.TokenConverter.ImpCov

/**
  * TrivialPlot parser lint.
  *
  * @author Eric Zhao
  */
object TpLint {

  def lint(str: String): Unit = println(str)

  def lintWith(msg: String): TpLint = {
    lint(s"Suggestion: $msg")
    new TpLint
  }

  def lintLet(x: String, y: Token): Unit = {
    lintWith("Use let clause for assignment instead")
      .origin(0, s"$x = ${y.string}")
      .whyNot(s"let $x = ${y.string}")
  }
}

class TpLint {
  import TpLint.lint

  def origin(line: Int, ori: String): TpLint = {
    lint(s"-- In line $line")
    lint(s"Found:")
    lint(s"  $ori")
    this
  }

  def origin(ori: String): TpLint = {
    lint(s"Found:")
    lint(s"  $ori")
    this
  }

  def whyNot(advice: String): TpLint = {
    lint(s"Why not:\n  $advice")
    this
  }
}


