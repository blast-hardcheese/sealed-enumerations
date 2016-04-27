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
