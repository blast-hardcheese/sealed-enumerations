SealedEnumerations for Scala
============================

Traits and helpers for dealing with a `sealed trait` alternative to Scala's built-in `Enumeration` class.

Why
---

Scala `Enumeration`s are extremely convenient, and have a nice interface to work with. Unfortunately, we ran into some issues:

1. Enumeration values are not `sealed`. As such, we can't get inexhaustive pattern match warnings.
1. Enumerations are not safe. `Enum.withName(s: String)` assumes membership, which makes common idioms (`Option[T]`, `flatMap`) cumbersome.

Additionally, we found we were re-implementing common tasks when working with other libraries:

1. `SealedEnumeration` <=> [`slick`](https://github.com/slick/slick) [`JdbcType`](https://github.com/slick/slick/blob/master/slick/src/main/scala/slick/jdbc/JdbcType.scala) conversions
1. `GetResult`s and `SetParameter`s for `[T]`, `[Option[T]]`, and `[List[T]]`
1. Play `Format[T]`, `QueryStringBindable[T]`, `PathBindable[T]` instances

Thank you
---------

Thanks to @laser and @papaver

Example usage
-------------

We recommend that you define the following trait:

```scala
trait SealedEnumeration
  extends SealedOrderedEnumeration
     with SealedBindableEnumeration
     with SealedJsonEnumeration
     with BaseSealedEnumeration
```

This allows you to choose which libraries you want to integrate with.

### `SealedEnumeration` (no Slick extensions):

```scala
object VideoQuality extends SealedEnumeration {
  sealed trait Value extends ValueMixin

  case object High extends Value { val value = "high" }
  case object Medium extends Value { val value = "medium" }
  case object Low extends Value { val value = "low" }

  val values = Vector(
    High,
    Medium,
    Low
  )
}
```

### `SealedDBEnumeration`

Requires a `name` to specify the database type to map to. If you are not using enumerations in your database, `StringyDBEnumeration` maps to `text`.

```scala
object Intervals extends SealedDBEnumeration {
  sealed trait Value extends ValueMixin

  val name = "interval"
  override def default = Some(Intervals.Day)

  case object Hour extends Value { val value = "hour" }
  case object Day extends Value { val value = "day" }
  case object Week extends Value { val value = "week" }
  case object Month extends Value { val value = "month" }

  val values = Vector(
    Hour,
    Day,
    Week,
    Month
  )
}
```
