package com.github.nnnnusui
package format.json

import com.github.nnnnusui.format.Json

import scala.util.parsing.combinator.RegexParsers

private[format] object Parser extends RegexParsers{
  def apply(text: String): Either[String, Json] = ???
}