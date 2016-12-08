package io.sczyh30.tplot.interpreter

/**
  * Drawer class.
  */
class Drawer(context: Context) {

  def draw(): Unit = {

  }
}

object Drawer {
  implicit class SeqTypeclass[T](seq: Seq[T]) {
    def withContext(context: Context) = new Drawer(context)
  }
}
