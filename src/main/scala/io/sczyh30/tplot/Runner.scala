package io.sczyh30.tplot

import io.sczyh30.tplot.interpreter.Interpreter
import io.sczyh30.tplot.lexer.{Lexer, Token}
import io.sczyh30.tplot.parser.Parser
import io.sczyh30.tplot.util.Show._

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
  * TrivialPlot interpreter runner.
  *
  * @author Eric Zhao
  */
object Runner {
  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      println("Please provide the source path")
    } else {
      runFrom(args(0))
    }
  }

  def testLexer(filename: String): List[Token] = {
    val src = Source.fromFile(filename).getLines.mkString("\n")
    new Lexer().go(src.toCharArray)
  }

  def testParser(filename: String): Unit = {
    val src = Source.fromFile(filename).getLines.mkString("\n")
    println(s"AST:\n\n${new Parser().parse(new Lexer().go(src.toCharArray)).get.string}")
  }

  def testPoints(filename: String): Unit = {
    val src = Source.fromFile(filename).getLines.mkString("\n")
    val ast = new Parser().parse(new Lexer().go(src.toCharArray))
    ast foreach new Interpreter().interpret
  }

  def runFrom(filename: String): Unit = {
    val ast = for {
      src <- Try(Source.fromFile(filename).getLines.mkString("\n"))
      tokens = new Lexer().go(src.toCharArray)
      ast <- new Parser().parse(tokens)
    } yield ast
    ast match {
      case Success(unit) => println(s"AST:\n\n${unit.string}")
      case Failure(cause) => println(s"Parse Error:\n  ${cause.getMessage}")
    }
  }
}
