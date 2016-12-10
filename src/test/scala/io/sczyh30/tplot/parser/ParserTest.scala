package io.sczyh30.tplot.parser

import io.sczyh30.tplot.lexer._
import org.specs2.mutable.Specification
import org.specs2.specification.{AfterEach, BeforeEach}

/**
  * Test case for parser.
  *
  * @author Eric Zhao
  */
class ParserTest extends Specification with BeforeEach with AfterEach {

  val parser = new Parser()

  override protected def before: Any = {

  }

  override protected def after: Any = {

  }

  "Parser" should {
    "match the case (LET-ASSIGN, VECTOR)" in {
      val tc = List(LET, ATOM("origin"), IS, LP, NUMBER(300.0), COMMA, NUMBER(200.0), RP, SEMICOLON)
      parser parse tc must beSuccessfulTry
    }
  }
}
