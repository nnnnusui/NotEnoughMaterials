package com.github.nnnnusui
package format

object Json{
  def parse(text: String) = json.Parser(text)
  def stringify(json: Json) = ???
}
trait Json