package io.sczyh30.tplot.interpreter

/**
  * Global variables.
  */
object Globals {
  // Default values.
  val ORIGIN_DEFAULT: (Double, Double) = (0.0d, 0.0d)
  val ORIGIN_ROT = 0.0d
  val ORIGIN_SCALE: (Double, Double) = (1.0d, 1.0d)

  // Built-in variables and functions.
  val sin = (x: Double) => Math.sin(x)
  val cos = (x: Double) => Math.cos(x)
  val tan = (x: Double) => Math.tan(x)
  val log = (x: Double) => Math.log(x)
  val ln = (x: Double) => Math.log(x)
  val sigmoid = (x: Double) => 1.0d / (1.0d + Math.pow(Math.E, -x))
  val ReLU = (x: Double) => if (x < 0) 0 else x
}
