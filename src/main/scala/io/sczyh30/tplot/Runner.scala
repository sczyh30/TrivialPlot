package io.sczyh30.tplot

import io.sczyh30.tplot.lexer.{Lexer, Token}
import io.sczyh30.tplot.parser.Parser

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

  def test(filename: String): List[Token] = {
    val src = Source.fromFile(filename).getLines.mkString("\n")
    new Lexer().go(src.toCharArray)
  }

  def runFrom(filename: String): Unit = {
    /*val tk = for {
      fileContents <- Try(Source.fromFile(filename).getLines.mkString("\n"))
      tokens <- new Lexer().run(fileContents)
    } yield tokens*/
  }
}
