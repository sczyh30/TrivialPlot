package io.sczyh30.tplot.parser

import scala.language.implicitConversions

import io.sczyh30.tplot.lexer._
import io.sczyh30.tplot.lexer.TokenConverter.ImpCov
import io.sczyh30.tplot.parser.Parser._
import io.sczyh30.tplot.parser.ParseStream.ParseStreamTypeclass

import scala.util.{Failure, Success, Try}

/**
  * TrivialPlot parser class.
  *
  * @author Eric Zhao
  */
class Parser {

  def parseExpr(tokens: List[Token]): Result[Expr] = {
    ???
  }

  def parseTerm(tokens: List[Token]): Result[Expr] = {
    ???
  }

  def parseFactor(tokens: List[Token]): Result[Expr] = {
    ???
  }

  def parseDraw(tokens: List[Token]): Result[Stmt] = parseExpr(tokens) @~> SEMICOLON lift Draw

  def parseFor(tokens: List[Token]): Result[Stmt] = tokens match {
    case ATOM(ref) :: FROM :: rs =>
      for {
        (fromE, rs0) <- parseExpr(rs) @~> TO
        (toE, rs1) <- parseExpr(rs0) @~> STEP
        (stepE, rs2) <- parseExpr(rs1) @~> DRAW
        (draw, rsf) <- parseDraw(rs2)
      } yield (For(Ref(ref), fromE, toE, stepE, draw), rsf)
  }

  def parseAssign(tokens: List[Token]): Result[Stmt] = tokens match {
    case ATOM(ref) :: IS :: rs =>
      for {
        (expr, rtks) <- parseExpr(rs)
      } yield (Assign(Ref(ref), expr), rtks)
    case _ => Bad(s"Unexpected error when parsing `let` clause")
  }

  def parseStmt(tokens: List[Token]): Result[Stmt] = tokens match {
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
      case (List(END), List()) => List()
      case (List(), _) => stmts.reverse
      case (_, _) => parseStmt(tokens) match {
        case succ@Success((_, rs)) => parseR(rs, succ :: stmts)
        case err@Failure(_) => List(err)
      }
    }

  def parse(tokens: List[Token]): Result[TranslationUnit] = generateAST(parseR(tokens, List()))

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
