package com.github.nnnnusui
package format

sealed trait Json
object Json{
  def parse(text: Predef.String) = json.Parser(text)
  def stringify(json: Json) = ???

  sealed trait Value extends Json
  case class  Object(value: Map[Predef.String, Value]) extends Value {
    def get(key: Predef.String): Option[Value] = value.get(key)
  }
  case class  Array (value: List[Value]  ) extends Value
  case class  String(value: Predef.String) extends Value
  case class  Number(value: Double       ) extends Value
  case object True  extends Value
  case object False extends Value
  case object Null  extends Value
}