package com.talenthouse.enum

import scala.reflect.ClassTag

import slick.jdbc.{ GetResult, JdbcType, SetParameter }

trait SealedSlickEnumeration[ValueType] {
  self: BaseSealedEnumeration =>

  val name: String
  def toLocal: ValueType => Value
  def toRemote: Value => ValueType

  implicit def typeMapper(implicit tag: ClassTag[Value]): JdbcType[Value]
  implicit def listTypeMapper(implicit tag: ClassTag[Value]): JdbcType[List[Value]]

  // Workaround for Singleton types having priority, confusing Slick.
  // Other people have explained the problem here:
  //   http://stackoverflow.com/a/34636852/743614
  //
  // `widen` refers to broadening the type from `Singleton` (only one
  // inhabitant) to our `Value` type which is what we provide mappings
  // for via typeMapper.
  implicit class RichDBValue(x: Value) {
    def widen: Value = x
  }
}

trait SealedPlainSlickEnumeration[ValueType] {
  self: BaseSealedEnumeration =>

  implicit def getEnum(implicit tag: ClassTag[Value]): GetResult[Value]
  implicit def getEnumOption(implicit tag: ClassTag[Value]): GetResult[Option[Value]]
  implicit def getEnumArray(implicit tag: ClassTag[Value]): GetResult[List[Value]]
  implicit def getEnumArrayOption(implicit tag: ClassTag[Value]): GetResult[Option[List[Value]]]

  implicit def setEnum(implicit tag: ClassTag[Value]): SetParameter[Value]
  implicit def setEnumOption(implicit tag: ClassTag[Value]): SetParameter[Option[Value]]
  implicit def setEnumArray(implicit tag: ClassTag[Value]): SetParameter[List[Value]]
  implicit def setEnumArrayOption(implicit tag: ClassTag[Value]): SetParameter[Option[List[Value]]]
}

trait BaseSealedDBEnumeration
  extends BaseSealedEnumeration
     with SealedSlickEnumeration[String]
     with SealedPlainSlickEnumeration[String] {

  def toLocal: String => Value = unsafeWithName _
  def toRemote: Value => String = _.value
}

trait BaseSealedDBIntEnumeration
  extends BaseSealedEnumeration
     with SealedSlickEnumeration[Int]
     with SealedPlainSlickEnumeration[Int] { self: BaseSealedEnumeration =>

  val name = "int4"
  type Value <: ValueMixin
  trait ValueMixin extends super.ValueMixin {
    val id: Int
  }


  def apply(id: Int): Value = values.find(_.id == id).get
  def toLocal: Int => Value = apply
  def toRemote: Value => Int = _.id
}

trait BaseStringyDBEnumeration
  extends BaseSealedDBEnumeration {

  val name = "text"
}
