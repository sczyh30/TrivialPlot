package io.sczyh30.tplot.interpreter

import io.sczyh30.tplot.lexer._
import io.sczyh30.tplot.parser._
import org.specs2.mutable.Specification

import scala.util.Success

/**
  * Test case for TrivialPlot runtime evaluation.
  *
  * @author Eric Zhao
  */
class EvalTest extends Specification {

  val i = new Interpreter

  "The TrivialPlot runtime evaluation" should {
    "evaluate the number" in {
      i eval Num(10.0) should successfulTry like {
        case Success(10.0d) => ok
      }
    }
    "evaluate the binary operator on scalar number" in {
      i eval BinOp(ADD, Num(2.0), Num(6.0)) should successfulTry like {
        case Success(8.0d) => ok
      }
    }
    "evaluate the unary operator on scalar number" in {
      i eval UnaryOp(SUB, Num(24.0)) should successfulTry like {
        case Success(-24.0d) => ok
      }
    }
    "evaluate the unary operator on binary operator" in {
      i eval UnaryOp(SUB, BinOp(MUL, Num(4.0), UnaryOp(SUB, Num(3.0)))) should successfulTry like {
        case Success(12.0d) => ok
      }
    }
    "evaluate the unary operator on function call" in {
      i eval UnaryOp(SUB, Funcall(Ref("ReLU"), Num(22.0))) should successfulTry like {
        case Success(-22.0) => ok
      }
    }
    "evaluate the trivial vector" in {
      i eval Vector2(Num(6.0), UnaryOp(SUB, Num(4.0))) should successfulTry like {
        case Success((6.0, -4.0)) => ok
      }
    }
    "evaluate a built-in function call" in {
      i eval Funcall(Ref("ReLU"), Num(8.0)) should successfulTry like {
        case Success(8.0) => ok
      }
    }
    "first assign a new variable then evaluate" in {
      i.assign(Ref("moha"), Vector2(Num(1.0), Num(3.0)))
      i eval Ref("moha") should successfulTry like {
        case Success((1.0, 3.0)) => ok
      }
    }
  }
}
