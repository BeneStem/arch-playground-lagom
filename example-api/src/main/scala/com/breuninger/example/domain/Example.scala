package com.breuninger.example.domain

import play.api.libs.json.{Format, Json}

case class Example(id: String)

object Example {
  implicit val format: Format[Example] = Json.format[Example]
}
