package io.sczyh30.tplot.lexer

import org.specs2.mutable.Specification

/**
  * Test case for lexer class.
  *
  * @author Eric Zhao
  */
class LexerTest extends Specification {

  val lexer = new Lexer

  "The TrivialPlot lexer functions" should {
    "detect the keywords" in {
      lexer.extractAtom("let x = 5".toCharArray) should beLike {
        case (LET, rs) if rs.length == " x = 5".length => ok
      }
    }

    "detect the atoms if not keywords" in {
      lexer.extractAtom("xs = 5".toCharArray) should beLike {
        case (x, rs) if x == ATOM("xs") => ok
      }
    }

    "recognize the numbers" in {
      lexer.isNumElem('9') should beTrue
      lexer.isNumElem('.') should beTrue
      lexer.isNumElem('+') should not(beTrue)
    }

    "recognize the spaces" in {
      lexer.isSpace(' ') should beTrue
      lexer.isSpace('\n') should beTrue
      lexer.isSpace(',') should not(beTrue)
    }

  }

  "The TrivialPlot lexer" should {
    "parse the code correctly and generate token sequence" in {
      "parse the assign clause correctly" in {
        val tokens = lexer.go("origin is (300, 300);".toCharArray)
        tokens should beLike {
          case ATOM("origin") :: IS :: LP :: NUMBER(300) :: COMMA :: NUMBER(300)
            :: RP :: SEMICOLON :: EOF :: Nil => ok
        }
      }

      "parse the for clause correctly" in {
        val tokens = lexer.go("for T from -20*E to 20*E step 0.1 draw (T, -ReLU(T));".toCharArray)
        tokens should beLike {
          case FOR :: ATOM("T") :: FROM :: SUB :: NUMBER(20.0) :: MUL :: ATOM("E") :: TO
            :: NUMBER(20.0) :: MUL :: ATOM("E") :: STEP :: NUMBER(0.1)
            :: DRAW :: LP :: ATOM("T") :: COMMA :: SUB :: ATOM("ReLU")
            :: LP :: ATOM("T") :: RP :: RP :: SEMICOLON :: EOF :: Nil => ok
        }
      }
    }
  }
}
