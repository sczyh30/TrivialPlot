package io.sczyh30.tplot.parser

import io.sczyh30.tplot.lexer._
import org.specs2.mutable.Specification

import scala.util.{Success, Failure}

/**
  * Test case for parser class.
  *
  * @author Eric Zhao
  */
class ParserTest extends Specification {

  val parser = new Parser()

  "The TrivialPlot parser functions" should {
    ok("Waiting for test")
  }

  "The TrivialPlot parser" should {
    "match the case (LET-ASSIGN, VECTOR) and generate corresponding AST" in {
      val tokens = List(LET, ATOM("origin"), IS, LP, NUMBER(300.0), COMMA, NUMBER(200.0), RP, SEMICOLON, EOF)
      parser parse tokens must beSuccessfulTry like {
        case Success(TranslationUnit(List(
        Assign(Ref("origin"), Vector2(Num(300.0), Num(300.0)))
        ))) => ok
      }
    }
  }
}
