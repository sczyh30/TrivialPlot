name := "TrivialPlot"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "com.chuusai" % "shapeless_2.12" % "2.3.2"
)

initialCommands := """|import io.sczyh30.tplot.Runner
                      |import io.sczyh30.tplot.lexer.Lexer
                      |import scala.util.Success
                      |import scala.util.Failure
                      |
                      |import scala.language.implicitConversions
                      |
                      |val lexer = new Lexer()
                      |implicit def str2ArrayImplicit(str: String): Array[Char] = str.toCharArray
                      |
                      |""".stripMargin