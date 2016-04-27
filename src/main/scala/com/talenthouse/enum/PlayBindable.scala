package com.talenthouse.enum

import play.api.libs.json.{ Json, JsString }
import play.api.mvc.{ JavascriptLiteral, PathBindable, QueryStringBindable }

import com.netaporter.uri.{ PathPart, QueryString }
import com.netaporter.uri.config.UriConfig

trait SealedBindableEnumeration {
  self: BaseSealedEnumeration =>

  implicit def queryStringBinder = new QueryStringBindable[Value] {
    def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, Value]] = {
      for {
        values <- params.get(key)
        value <- values.headOption
      } yield {
        withName(value)
          .toRight(s"Invalid value for $key: $value")
      }
    }

    def unbind(key: String, o: Value): String = {
      QueryString(Seq((key, Some(o.value))))
        .queryToString(UriConfig.default)
        .drop(1) // Drop leading `?'
    }
  }

  implicit def pathBindable = new PathBindable[Value] {
    def unbind(key: String, value: Value): String = {
      PathPart(value.value)
        .partToString(UriConfig.default)
    }

    def bind(key: String, value: String): Either[String, Value] = {
      withName(value)
        .toRight(s"Invalid value for $key: $value")
    }
  }

  implicit def jsLiteral: JavascriptLiteral[Value] = new JavascriptLiteral[Value] {
    def to(value: Value) = Json.stringify(JsString(value.value))
  }
}
