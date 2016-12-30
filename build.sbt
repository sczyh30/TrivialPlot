name := "TrivialPlot"

version := "0.1.1"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.12.1",
  "org.scala-lang.modules" % "scala-swing_2.12" % "2.0.0-M2",
  "org.specs2" % "specs2-core_2.12" % "3.8.6" % "test"
)

initialCommands := """|import io.sczyh30.tplot.Runner
                      |import io.sczyh30.tplot.lexer._
                      |import io.sczyh30.tplot.parser._
                      |import io.sczyh30.tplot.interpreter._
                      |import scala.util.Success
                      |import scala.util.Failure
                      |
                      |import scala.language.implicitConversions
                      |
                      |val lexer = new Lexer()
                      |val parser = new Parser()
                      |implicit def str2ArrayImplicit(str: String): Array[Char] = str.toCharArray
                      |""".stripMargin

scalacOptions in Test ++= Seq("-Yrangepos")