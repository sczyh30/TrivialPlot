package io.sczyh30.tplot.lexer

import scala.language.implicitConversions
import scala.collection.immutable.HashMap
import Character.{isDigit, isAlphabetic => isAlpha, isSpaceChar}

import scala.reflect.ClassTag

/**
  * TrivialPlot lexer class.
  *
  * @author Eric Zhao
  */
class Lexer {
  // Implicit conversion for convenience
  implicit def seq2ArrImplicit[T: ClassTag](seq: Seq[T]): Array[T] = seq.toArray

  implicit def double2TkImplicit(d: Double): Token = NUMBER(d)

  implicit def array2StrImplicit(cs: Array[Char]): String = cs.mkString

  implicit def str2ArrayImplicit(str: String): Array[Char] = str.toCharArray

  val keywordMap = HashMap(
    "is" -> IS,
    "=" -> IS,
    "origin" -> ORIGIN,
    "scale" -> SCALE,
    "rot" -> ROT,
    "where" -> WHERE,
    "color" -> COLOR,
    "step" -> STEP,
    "draw" -> DRAW,
    "for" -> FOR,
    "from" -> FROM,
    "to" -> TO,
    "let" -> LET,
    "," -> COMMA,
    ";" -> SEMICOLON,
    "{" -> LB,
    "}" -> RB,
    "(" -> LP,
    ")" -> RP,
    "&&" -> AND,
    "||" -> OR,
    "!" -> NOT,
    "==" -> EQ,
    "!=" -> NE,
    "<" -> LT,
    ">" -> GT,
    ">=" -> GE,
    "<=" -> LE,
    "+" -> ADD,
    "-" -> MINUS,
    "*" -> MUL,
    "/" -> DIV
  )

  /**
    * Validate if the char is space or linefeed(`\n`).
    *
    * @param c char
    */
  def isSpace(c: Char): Boolean = isSpaceChar(c) || c == '\n'

  def isNumElem(c: Char): Boolean = isDigit(c) || c == '.'

  def isAtomElem(c: Char): Boolean = isAlpha(c) || isDigit(c) /* To review */

  def extractNumber(src: Array[Char]): (Token, Array[Char]) = {
    val remainsRaw = src.dropWhile(isNumElem)
    val valid = remainsRaw.length == 0 || !isAlpha(remainsRaw(0))
    val tk: Token = if (valid)
      src.takeWhile(isNumElem).mkString.toDouble
    else UNKNOWN(src.takeWhile(c => !isAlpha(c)))

    val remains = if (valid) remainsRaw
    else remainsRaw.dropWhile(c => isAlpha(c))
    // TODO: Should support postfix like `12.3L`
    (tk, remains)
  }

  def detectUnary(c: Char): Token = detectAtom(Array(c))(UNKNOWN)

  def detectAtom(src: Array[Char])(f: String => Token): Token = {
    keywordMap get src match {
      case Some(tk) => tk
      case None => f(src)
    }
  }

  def extractAtom(src: Array[Char]): (Token, Array[Char]) = {
    (detectAtom(src.takeWhile(isAtomElem))(ATOM), src.dropWhile(isAtomElem))
  }

  def skipComment(src: Array[Char]): Array[Char] = {
    src.dropWhile(_ != '\n').drop(1)
  }

  def fuckme(src: Array[Char]): List[Token] = src match {
    case Array() => List()
    case Array('/', '/', remains@_*) => fuckme(skipComment(remains))
    case Array('-', '-', remains@_*) => fuckme(skipComment(remains))
    case Array('&', '&', remains@_*) => AND :: fuckme(remains)
    case Array('|', '|', remains@_*) => OR :: fuckme(remains)
    case Array('!', '=', remains@_*) => NE :: fuckme(remains)
    case Array('=', '=', remains@_*) => EQ :: fuckme(remains)
    case Array('<', '=', remains@_*) => LE :: fuckme(remains)
    case Array('>', '=', remains@_*) => GE :: fuckme(remains)
    case Array(curr, remains@_*) if isSpace(curr) => fuckme(remains)
    case src@Array(curr, _*) if isDigit(curr) =>
      val (num, remains) = extractNumber(src)
      num :: fuckme(remains)
    case src@Array(curr, _*) if isAlpha(curr) =>
      val (tk, remains) = extractAtom(src)
      tk :: fuckme(remains)
    case Array(curr, remains@_*) => detectUnary(curr) :: fuckme(remains)
  }

}
