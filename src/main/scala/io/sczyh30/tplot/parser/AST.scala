package io.sczyh30.tplot.parser

import io.sczyh30.tplot.lexer.Token

/**
  * TrivialPlot abstract syntax tree(AST).
  */
sealed trait ASTNode

/**
  * Expression ADT.
  */
sealed trait Expr extends ASTNode

case class UnaryOp(op: Token, x: Expr) extends Expr
case class BinOp(op: Token, left: Expr, right: Expr) extends Expr
case class Ref(name: String) extends Expr
case class Num(x: Double) extends Expr
case class Funcall(f: Expr) extends Expr
case class Vector2(x: Expr, y: Expr) extends Expr

/**
  * Statement ADT.
  */
sealed trait Stmt extends ASTNode

case class TranslationUnit(stmts: List[Stmt]) extends Stmt

case class Assign(ref: Expr, value: Expr) extends Stmt
case class For(ref: Expr, from: Expr, to: Expr, step: Expr, call: Stmt) extends Stmt
case class Where(stmt: Stmt) extends Stmt
case class Draw(f: Expr) extends Stmt
