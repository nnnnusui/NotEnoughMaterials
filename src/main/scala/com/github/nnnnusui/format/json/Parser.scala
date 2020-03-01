package com.github.nnnnusui
package format.json

import com.github.nnnnusui.format.Json

import scala.util.parsing.combinator.RegexParsers

private[format] object Parser extends RegexParsers{
  def apply(text: String): Either[String, Json] =
    parse(element, text) match {
      case Success( matched,_) => Right(matched)
      case Failure( msg    ,_) => Left(s"FAILURE: $msg")
      case Error  ( msg    ,_) => Left(s"ERROR: $msg")
    }
  val element = ".*".r ^^ (it=> Dummy(s"element: $it"))
}
case class Dummy(value: String) extends Json