package io.sczyh30.tplot.parser

import org.specs2.mutable.Specification
import org.specs2.specification.{AfterEach, BeforeEach}

/**
  * Test case for parser.
  *
  * @author Eric Zhao
  */
class ParserTest extends Specification with BeforeEach with AfterEach {

  override protected def before: Any = {

  }

  override protected def after: Any = {

  }

  "ParserTest" should {
    "$u2202$times" in {
      ok
    }

  }
}
