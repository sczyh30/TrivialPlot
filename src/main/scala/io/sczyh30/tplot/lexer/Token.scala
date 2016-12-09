package io.sczyh30.tplot.lexer

/**
  * Token ADT.
  */
sealed trait Token

case class ATOM(x: String) extends Token
case class NUMBER(x: Double) extends Token
case class UNKNOWN(part: String) extends Token
case object ERROR extends Token
case object END extends Token

case object COMMA extends Token
case object SEMICOLON extends Token
case object LB extends Token
case object RB extends Token
case object LP extends Token
case object RP extends Token

case object ORIGIN extends Token
case object SCALE extends Token
case object IS extends Token
case object ROT extends Token
case object WHERE extends Token
case object COLOR extends Token
case object STEP extends Token
case object DRAW extends Token
case object FOR extends Token
case object FROM extends Token
case object TO extends Token
case object LET extends Token

case object AND extends Token
case object OR extends Token
case object NOT extends Token
case object EQ extends Token
case object NE extends Token
case object LT extends Token
case object GT extends Token
case object LE extends Token
case object GE extends Token

case object ADD extends Token
case object MINUS extends Token
case object MUL extends Token
case object DIV extends Token
case object POWER extends Token
