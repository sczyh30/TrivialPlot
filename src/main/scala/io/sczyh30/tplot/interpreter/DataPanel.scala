package io.sczyh30.tplot.interpreter

import java.awt.geom.Line2D

import scala.swing.{Graphics2D, Panel}

class DataPanel(data: List[(Double, Double)]) extends Panel {

  override protected def paintComponent(g: Graphics2D): Unit = {
    data.foreach {
      case (x,y) => g.draw(new Line2D.Double(x, y, x, y))
    }
  }
}
