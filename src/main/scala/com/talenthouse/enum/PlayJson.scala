package com.talenthouse.enum

import play.api.libs.json._

trait SealedJsonEnumeration {
  self: BaseSealedEnumeration =>

  implicit def jsonFormat = new Format[Value] {
    def reads(o: JsValue): JsResult[Value] = o match {
      case JsString(name) => JsSuccess(unsafeWithName(name))
      case _ => JsError(s"""[${this.getClass.getSimpleName}] Unknown value "${o}"""")
    }
    def writes(o: Value): JsValue = JsString(o.value)
  }
}
