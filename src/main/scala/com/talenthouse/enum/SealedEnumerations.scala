package com.talenthouse.enum

trait BaseSealedEnumeration {
  trait ValueMixin {
    val value: String
  }

  type Value <: ValueMixin

  def default: Option[Value] = None

  def values: Vector[Value]

  def withName(name: String): Option[Value] = {
    values.find(_.value == name)
  }

  def unsafeWithName(value: String): Value = {
    withName(value)
      .orElse(default)
      .getOrElse {
        throw new Exception(s"""[${this.getClass.getSimpleName}] Unknown value "${value}"""")
      }
  }
}

trait SealedOrderedEnumeration {
  self: BaseSealedEnumeration =>

  implicit val ord = new Ordering[Value] {
    def compare(ours: Value, theirs: Value): Int = values.indexOf(ours) compare values.indexOf(theirs)
  }
}
