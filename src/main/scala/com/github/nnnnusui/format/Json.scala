package com.github.nnnnusui
package format

sealed trait Json
object Json{
  def parse(text: Predef.String) = json.Parser(text)
  def stringify(json: Json) = ???


  case class Member(key: String, value: Element) extends Json
  case class Element(value: Value) extends Json

  sealed trait Value extends Json
  case class  Object(value: List[Member] ) extends Value
  case class  Array (value: List[Element]) extends Value
  case class  String(value: Predef.String) extends Value
  case class  Number(value: Double       ) extends Value
  case object True  extends Value
  case object False extends Value
  case object Null  extends Value
}