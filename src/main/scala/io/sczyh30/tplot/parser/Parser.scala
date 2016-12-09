package io.sczyh30.tplot.parser

import scala.language.implicitConversions

import io.sczyh30.tplot.lexer._
import io.sczyh30.tplot.lexer.TokenConverter.ImpCov
import io.sczyh30.tplot.parser.Parser._
import io.sczyh30.tplot.parser.ParseStream._

import scala.util.{Failure, Success, Try}

/**
  * TrivialPlot parser class.
  *
  * @author Eric Zhao
  */
class Parser {

  /**
    * Parse arbitrary times recursively.
    *
    * @param tokens sequence of tokens
    * @param test   test predicate
    * @param f      apply function
    * @return parse result
    */
  def ∂*(tokens: List[Token])(test: Token => Boolean)
        (f: List[Token] => Result[Expr]): Result[Expr] = tokens match {
    case c :: rs if test(c) => f(rs) flatMap {
      case (left, rsf) => ∂*(rsf)(test)(f) match {
        // TODO: use `recover` to refine here
        case Success((right, rss)) => Success(BinOp(c, left, right), rss)
        case Failure(_) => Success(left, rsf)
      }
    }
    case _ => Bad("Not match for ∂*")
  }

  def exprPredicate(x: Token): Boolean = x.eq(ADD) || x.eq(SUB)

  def termPredicate(x: Token): Boolean = x.eq(MUL) || x.eq(DIV)

  def parseExpr(tokens: List[Token]): Result[Expr] = {
    parseTerm(tokens) flatMap {
      case (l, rs) => ∂*(rs)(exprPredicate)(parseTerm) match {
        case Success((r, rss)) => Success(BinOp(rs.head, l, r), rss)
        case Failure(_) => Success(l, rs)
      }
    }
  }

  def parseTerm(tokens: List[Token]): Result[Expr] = {
    parseFactor(tokens) flatMap {
      case (l, rs) => ∂*(rs)(termPredicate)(parseFactor) match {
        case Success((r, rss)) => Success(BinOp(rs.head, l, r), rss)
        case Failure(_) => Success(l, rs)
      }
    }
  }

  def parseFactor(tokens: List[Token]): Result[Expr] = tokens match {
    case ADD :: rs => parseFactor(rs)
    case SUB :: rs => parseFactor(rs) <#> UnaryOp(SUB)
    case _ => parsePowerOp(tokens)
  }

  def parsePowerOp(tokens: List[Token]): Result[Expr] = parseAtom(tokens) flatMap {
    case (left, next :: rs) if next.eq(POWER) =>
      parsePowerOp(rs) map {
        case (right, rsf) => (BinOp(POWER, left, right), rsf)
      }
    case (atom, rs) => Success(atom, rs)
  }

  def parseAtom(tokens: List[Token]): Result[Expr] = tokens match {
    case ATOM(refName) :: rs => Success(Ref(refName), rs)
    case NUMBER(n) :: rs => Success(Num(n), rs)
    case _ => ???
  }

  def parseDraw(tokens: List[Token]): Result[Stmt] = parseExpr(tokens) <#> Draw

  def parseFor(tokens: List[Token]): Result[Stmt] = tokens match {
    case ATOM(ref) :: rs =>
      for {
        (fromE, rs0) <- FROM @|~> parseExpr(rs)
        (toE, rs1) <- TO @|~> parseExpr(rs0)
        (stepE, rs2) <- STEP @|~> parseExpr(rs1)
        (draw, rsf) <- DRAW @|~> parseDraw(rs2)
      } yield (For(Ref(ref), fromE, toE, stepE, draw), rsf)
    case _ => Bad("Unexpected error when parsing `for` statement")
  }

  def parseAssign(tokens: List[Token]): Result[Stmt] = tokens match {
    case Nil => Bad("Unexpected error when parsing `let` clause")
    case ATOM(ref) :: IS :: rs =>
      for {
        (expr, rtks) <- parseExpr(rs)
      } yield (Assign(Ref(ref), expr), rtks)
    case t :: _ => Bad(s"Unexpected error when parsing `let` clause (token: ${t.string})")
  }

  def parseStmt(tokens: List[Token]): Result[Stmt] = tokens match {
    case Nil => Bad(s"Expected statement keywords but not found")
    case FOR :: rs => parseFor(rs)
    case LET :: rs => parseAssign(rs)
    // case WHERE :: rs =>
    case ATOM(x) :: IS :: y :: _ =>
      TpLint.lintLet(x, y)
      parseAssign(tokens)
    case SEMICOLON :: rs => parseStmt(rs)
    case e :: _ => Bad(s"Expected statement keywords but not match: ${e.string}")
  }

  /**
    * Parse tokens recursively.
    *
    * @param tokens sequence of tokens
    * @param stmts  current sequence of statements
    * @return sequence of parsed statements
    */
  def parseR(tokens: List[Token], stmts: List[Result[Stmt]]): List[Result[Stmt]] =
    (tokens, stmts) match {
      case (List(END), Nil) => Nil
      case (List(END), _) => stmts.reverse
      case (_, _) => parseStmt(tokens) match {
        case succ@Success((_, rs)) => parseR(rs, succ :: stmts)
        case err@Failure(_) => List(err)
      }
    }

  def parse(tokens: List[Token]): Result[TranslationUnit] = generateAST(parseR(tokens, Nil))

  def generateAST(list: List[Result[Stmt]]): Result[TranslationUnit] = {
    ???
  }

}

object Parser {
  // Use `Future` for convenience.
  type Result[+E] = Try[(E, List[Token])]
  // Data constructor for error.
  val Bad: (String) => Try[Nothing] =
    (x: String) => Failure(new RuntimeException(x))
}
