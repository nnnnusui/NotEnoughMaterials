package com.github.nnnnusui
package format

sealed trait Json{
  def option[A <: Json : Manifest]: Option[A] =
    if(manifest[A] == manifest[this.type]) Some(this.asInstanceOf[A])
    else                                   None
}
object Json{
  def parse(text: Predef.String): Either[Predef.String, Value] = json.Parser(text)
  def stringify(json: Json) = ???

  sealed trait Value extends Json
  case class  Object(value: Map[Predef.String, Value]) extends Value {
    def get(key: Predef.String): Option[Value] = value.get(key)
  }
  case class  Array (value: Seq[Value]   ) extends Value
  case class  String(value: Predef.String) extends Value
  case class  Number(value: Double       ) extends Value
  case object True  extends Value
  case object False extends Value
  case object Null  extends Value
}