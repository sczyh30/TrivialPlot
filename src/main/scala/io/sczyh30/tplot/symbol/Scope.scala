package io.sczyh30.tplot.symbol

import io.sczyh30.tplot.interpreter.Globals._

import scala.collection.immutable.HashMap

/**
  * Execution scope.
  */
class Scope(prev: Option[Scope] = None) {

  var table = HashMap(
    "PI" -> Math.PI,
    "E" -> Math.E,
    "origin" -> ORIGIN_DEFAULT,
    "rot" -> ORIGIN_ROT,
    "scale" -> ORIGIN_SCALE,
    "sin" -> sin,
    "cos" -> cos,
    "tan" -> tan,
    "log" -> log,
    "ln" -> ln,
    "sigmoid" -> sigmoid,
    "ReLU" -> ReLU
  )


}
