name := "TrivialPlot"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.chuusai" % "shapeless_2.11" % "2.3.2",
  "underscoreio" %% "doodle" % "0.6.5"
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
                      |""".stripMargin

resolvers += Resolver.bintrayRepo("underscoreio", "training")