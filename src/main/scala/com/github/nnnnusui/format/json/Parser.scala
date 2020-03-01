package com.github.nnnnusui
package format.json

import com.github.nnnnusui.format.Json

import scala.util.matching.Regex
import scala.util.parsing.combinator.RegexParsers

private[format] object Parser extends RegexParsers{
  def apply(text: String): Either[String, Json] =
    parse(element, text) match {
      case Success( matched,_) => Right(matched)
      case Failure( msg    ,_) => Left(s"FAILURE: $msg")
      case Error  ( msg    ,_) => Left(s"ERROR: $msg")
    }

  override def skipWhitespace: Boolean = false
  def ws: Parser[Option[String]] = opt(whiteSpace)

  def element: Parser[Json.Value] = ws ~> value <~ ws

  def value:   Parser[Json.Value]   = _object | array | string | number | _true | _false | _null
  def _object: Parser[Json.Object]  = "{"   ~> members    <~ "}"  ^^ (it=> Json.Object(it))
  def array:   Parser[Json.Array]   = "["   ~> elements   <~ "]"  ^^ (it=> Json.Array(it))
  def string:  Parser[Json.String]  = "\""  ~> characters <~ "\"" ^^ (it=> Json.String(it))
  def number:  Parser[Json.Number]  =          double             ^^ (it=> Json.Number(it))
  val _true:  Parser[Json.True.type]  =       "true"  ^^ (_=> Json.True)
  val _false: Parser[Json.False.type] =       "false" ^^ (_=> Json.False)
  val _null:  Parser[Json.Null.type]  =       "null"  ^^ (_=> Json.Null)

  def elements: Parser[List[Json.Value ]] = rep1sep(element, ",") | (ws ^^ (_=> List.empty))
  def members:  Parser[List[Json.Member]] = rep1sep(member , ",") | (ws ^^ (_=> List.empty))
  def member:   Parser[Json.Member]
    = (ws ~> string <~ ws) ~ (":" ~> element) ^^ {case string ~ element => Json.Member(string, element)}

  def characters: Parser[String] = rep(character) ^^ (_.mkString)
  def character:  Parser[String] = (".".r - "\"" - "\\") | "\\" ~> escape
  def escape:     Parser[String] = "\"" | "\\" | "/" | backspace | newPage | carriageReturn | linefeed | tab | unicode
  def backspace:      Parser[String] = "b" ^^ (_=> "\b")
  def newPage:        Parser[String] = "f" ^^ (_=> "\f")
  def carriageReturn: Parser[String] = "n" ^^ (_=> "\n")
  def linefeed:       Parser[String] = "r" ^^ (_=> "\r")
  def tab:            Parser[String] = "t" ^^ (_=> "\t")
  def unicode:        Parser[String] = "u" ~> repN(4, hex) ^^ (it=> BigInt(it.mkString, 16).toChar.toString)
  def hex:            Parser[String] = digit | "[A-F]".r | "[a-f]".r

  def double:  Parser[Double]
    = integer ~ opt(fraction) ~ opt(exponent) ^^ { case integer ~ fraction ~ exponent =>
        s"$integer${fraction.getOrElse("")}${exponent.getOrElse("")}".toDouble
      }
  def integer:  Parser[String] = natural | digit | minus
  def minus:    Parser[String] = "-" ~> (digit | natural) ^^ (it=> s"-$it")
  def natural:  Parser[String] = oneToNine ~ digits ^^ { case head ~ tail => s"$head$tail" }

  def digits: Parser[String] = rep1(digit) ^^ (_.mkString)
  def digit:  Parser[String] = "0" | oneToNine
  def oneToNine: Regex       = "[1-9]".r
  def fraction: Parser[String] = '.' ~> digits ^^ (it=> s".$it")
  def exponent: Parser[String] = ("E" | "e") ~> opt(sign) ~ digits ^^ {case sign ~ digits => s"e${sign.getOrElse("")}$digits"}
  def sign:     Parser[String] = "+" | "-"
}