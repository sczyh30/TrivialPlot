package io.sczyh30.tplot.symbol

import scala.collection.immutable.HashMap

/**
  * Execution scope.
  */
class Scope(prev: Option[Scope] = None) {
  val table = new HashMap[String, AnyVal]
}
