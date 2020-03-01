package com.github.nnnnusui
package format

class JsonTest extends org.scalatest.FunSuite {
  val result = Json.parse(
    """test""".stripMargin)
  println(result)
}