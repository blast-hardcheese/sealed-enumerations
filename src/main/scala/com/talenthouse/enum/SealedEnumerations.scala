package com.talenthouse.enum

trait BaseSealedEnumeration {
  trait ValueMixin {
    val value: String
    @deprecated("Please use .value instead!", "1.0")
    override def toString: String = value
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

  @deprecated("Please use Values directly instead of String comparison", "1.0")
  def asStringSet: Set[String] = values.map(_.value).toSet

  implicit class RichValue(x: Value) {
    @deprecated("Please figure out a way to do this differently", "1.0")
    def id: Int = values.indexOf(x)
  }
}

trait SealedOrderedEnumeration {
  self: BaseSealedEnumeration =>

  implicit val ord = new Ordering[Value] {
    def compare(ours: Value, theirs: Value): Int = values.indexOf(ours) compare values.indexOf(theirs)
  }
}
