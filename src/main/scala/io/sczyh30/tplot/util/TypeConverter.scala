package io.sczyh30.tplot.util

import scala.reflect.runtime.universe.{TypeTag, Type, typeOf}

/**
  * Strict type checker and converter using reflection.
  */
object TypeConverter {
  // Strict type check (without type erase)
  implicit class TypeCov[T : TypeTag](x: T) {

    // def is[T2 : TypeTag]: Boolean = typeOf[T] =:= typeOf[T2]

    def is[T2]: Boolean = x.isInstanceOf[T2]

    def as[T2]: T2 = x.asInstanceOf[T2]

    def ª: Type = typeOf[T]
  }
}
