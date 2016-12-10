package io.sczyh30.tplot.interpreter

import swing.{MainFrame, SimpleSwingApplication}
import java.awt.Dimension

import io.sczyh30.tplot.lexer.Lexer
import io.sczyh30.tplot.parser.Parser

import scala.io.Source

/**
  * Drawer runner application.
  *
  * @author Eric Zhao
  */
object Drawer extends SimpleSwingApplication {

  def top = new MainFrame { // TODO: enhance error handling
    val src = Source.fromFile("src/test/resources/common.tp").getLines.mkString("\n")
    val ast = new Parser().parse(new Lexer().go(src.toCharArray))
    val data = ast.map(new Interpreter().interpret).get
    title = "TrivialPlot Runtime"
    contents = new DataPanel(data) {
      preferredSize = new Dimension(1000, 1000)
    }
  }
}
