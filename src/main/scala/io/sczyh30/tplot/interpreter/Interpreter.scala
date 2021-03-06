package io.sczyh30.tplot.interpreter

import io.sczyh30.tplot.symbol.Scope
import io.sczyh30.tplot.lexer._
import io.sczyh30.tplot.parser._
import io.sczyh30.tplot.parser.Parser.Bad
import io.sczyh30.tplot.util.TypeConverter.TypeCov

import scala.util.{Failure, Success, Try}

/**
  * TrivialPlot interpreter class.
  *
  * @author Eric Zhao
  */
final class Interpreter {

  // TODO: resolve type issues.

  val scope = new Scope()

  type Point2D = (Double, Double)
  var points: List[Point2D] = Nil

  def interpret(ast: TranslationUnit): List[Point2D] = {
    var stmts = ast.stmts
    while (stmts.nonEmpty) {
      stmts.head match {
        case Assign(ref, value) => assign(ref, value)
        case stmt@For(_, _, _, _, _) => prepareForDraw(stmt)
      }
      stmts = stmts.tail
    }
    invokeDraw
  }

  def invokeDraw: List[Point2D] = {
    println(s"Scope symbol table:\n${scope.table}\n")
    println(s"Points:\n$points")
    points
  }

  def prepareForDraw(stmt: For): Unit = {
    (evalNumber(stmt.from), evalNumber(stmt.to), evalNumber(stmt.step)) match {
      case (Success(start), Success(end), Success(step)) =>
        var cur: Double = start
        updateLocalVariable(stmt.ref, 0.0d)
        while (cur <= end) {
          val p = transformPoint(processDraw(stmt.call))
          cur += step
          points ::= p
          updateLocalVariable(stmt.ref, cur)
        }
      case _ =>
    }
  }

  def processDraw(stmt: Stmt): Point2D = stmt match {
    case Draw(x, y) =>
      (eval(x), eval(y)) match {
        case (Success(a), Success(b)) if a.is[Double] && b.is[Double] =>
          (a.as[Double], b.as[Double])
      }
  }

  def transformPoint(point: Point2D): Point2D = {
    val (scaleX, scaleY) = scope.table("scale").as[Point2D]
    val (ox, oy) = scope.table("origin").as[Point2D]
    val rot = scope.table("rot").as[Double]
    val sinR = Math.sin(rot)
    val cosR = Math.cos(rot)
    val (x1, y1) = (point._1 * scaleX, point._2 * scaleY)
    val (x2, y2) = (x1 * cosR + y1 * sinR, y1 * cosR + x1 * sinR)
    (x2 + ox, y2 + oy)
  }

  def updateLocalVariable(expr: Expr, good: Double): Unit = {
    expr match {
      case Ref(varN) => scope.table.get(varN) match {
        case Some(t) if t.is[Double] => scope.table += (varN -> good)
        case Some(f) if f.is[Double => Double] => // Function
        case None => scope.table += (varN -> 0.0d)
      }
    }
  }

  def assign(ref: Ref, expr: Expr): Unit = {
    eval(expr) match {
      case Success(x) => scope.table += (ref.name -> x)
      case Failure(_) => println(s"Type mismatch when assigning value to ${ref.name}: $expr")
    }
  }

  def evalNumber(expr: Expr): Try[Double] = {
    def tryMatch(expr: Expr): Double = {
      expr match {
        case Num(x) => x
        case Ref(name) if scope.table.exists(_.eq(name)) && scope.table(name).is[Double] =>
          scope.table(name).as[Double]
        case UnaryOp(SUB, x) =>
          if (x.isInstanceOf[Funcall]) evalFunc(x) match {
            case Success(ffx) => -ffx
          } else
            evalVariable(x) match {
              case Success(xx) if xx.is[Double] => -xx.as[Double]
              case _ => -tryMatch(x)
            }
        case BinOp(ADD, l, r) => pd(l, r)(_ + _)
        case BinOp(SUB, l, r) => pd(l, r)(_ - _)
        case BinOp(MUL, l, r) => pd(l, r)(_ * _)
        case BinOp(DIV, l, r) => pd(l, r)(_ / _)
        case BinOp(POWER, l, r) => pd(l, r)((x, y) => Math.pow(x, y))
      }
    }
    // TODO: refine this
    def pd(left: Expr, right: Expr)(op: (Double, Double) => Double): Double = {
      (evalVariable(left), evalVariable(right)) match {
        case (Success(l), Success(r)) if l.is[Double] && r.is[Double] =>
          op(l.as[Double], r.as[Double])
        case (Success(l), Failure(_)) if l.is[Double] =>
          op(l.as[Double], tryMatch(right))
        case (Failure(_), Success(r)) if r.is[Double] =>
          op(tryMatch(left), r.as[Double])
        case _ => op(tryMatch(left), tryMatch(right))
      }
    }

    Try(tryMatch(expr))
  }

  def evalVec(expr: Expr): Try[Point2D] = Try {
    expr match {
      case Vector2(x, y) => (evalNumber(x), evalNumber(y)) match {
        case (Success(x1), Success(x2)) => (x1, x2)
      }
    }
  }

  def evalFunc(expr: Expr): Try[Double] = Try {
    expr match {
      case Funcall(ref, e) => evalVariable(ref) match {
        case Success(x) if x.is[Double => Double] =>
          val func = x.as[Double => Double]
          eval(e) match {
            case Success(y) if y.is[Double] => func(y.as[Double])
          }
      }
    }
  }

  def evalVariable(expr: Expr): Try[Any] = expr match {
    case Ref(name) => scope.table.get(name) match {
      case Some(x) if x.is[Double] => Success(x.as[Double])
      case Some(x) if x.is[Point2D] => Success(x.as[Point2D])
      case Some(x) if x.is[Double => Double] => Success(x.as[Double => Double])
      case Some(_) => Bad("Type mismatch: unknown type discovered")
      case _ => Bad(s"Variable not defined: $name")
    }
    case _ => Bad("Type mismatch: expected Ref but found other")
  }

  /**
    * Evaluate the expression.
    * Dynamic type.
    *
    * @param expr expression
    * @return evaluation result
    */
  def eval(expr: Expr): Try[Any] = { // TODO: make this monadic
    evalFunc(expr).recoverWith {
      case _ => evalVariable(expr).recoverWith {
        case _ => evalVec(expr).recoverWith {
          case _ => evalNumber(expr)
        }
      }
    }
  }
}
