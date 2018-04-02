package com.breuninger.example.util

import play.api.libs.json._

object JsonFormats {
  def singletonReads[Object](singleton: Object): Reads[Object] = {
    (__ \ "value").read[String]
      .collect(JsonValidationError(s"Expected a JSON object with a single field with key 'value' and value '${singleton.getClass.getSimpleName}'")) {
        case s if s == singleton.getClass.getSimpleName => singleton
      }
  }

  def singletonWrites[Object]: Writes[Object] = Writes { singleton =>
    Json.obj("value" -> singleton.getClass.getSimpleName)
  }

  def singletonFormat[Object](singleton: Object): Format[Object] = {
    Format(singletonReads(singleton), singletonWrites)
  }
}
