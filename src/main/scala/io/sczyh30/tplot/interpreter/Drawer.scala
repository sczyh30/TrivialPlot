package io.sczyh30.tplot.interpreter

import doodle.core._
import doodle.core.Image._
import doodle.random._
import doodle.syntax._
import doodle.jvm.FileCanvas._
import doodle.jvm.Java2DCanvas._
import doodle.backend.StandardInterpreter._
import doodle.backend.Formats._

/**
  * Drawer class.
  */
class Drawer(context: Context) {
  Point
}

object Drawer {
  implicit class SeqTypeclass[T](seq: Seq[T]) {
    def withContext(context: Context) = new Drawer(context)
  }
}
