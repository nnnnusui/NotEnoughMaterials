package com.github.nnnnusui
package format

import java.nio.file.{Files, Paths}

class JsonTest extends org.scalatest.FunSuite {
  val testJson = Paths.get("resource", "test.json")
  import scala.collection.JavaConverters._
  val json = Files.readAllLines(testJson).asScala.mkString
  val parsed = Json.parse(json)
  println(parsed)
  val stringify = Json.stringify(parsed.right.get)
  println(stringify)
  val reParsed = Json.parse(stringify)
  println(reParsed)
  println(parsed == reParsed)
}