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
  def sin: (Double) => Double = (x: Double) => Math.sin(x)
  def cos: (Double) => Double = (x: Double) => Math.cos(x)
  def tan: (Double) => Double = (x: Double) => Math.tan(x)
  def log: (Double) => Double = (x: Double) => Math.log(x)
  def ln: (Double) => Double = (x: Double) => Math.log(x)
  def sigmoid: (Double) => Double = (x: Double) => 1.0d / (1.0d + Math.pow(Math.E, -x))
  def ReLU: (Double) => Double = (x: Double) => if (x < 0) 0 else x
}
