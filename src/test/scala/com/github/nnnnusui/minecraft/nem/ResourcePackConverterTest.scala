package com.github.nnnnusui.minecraft.nem

class ResourcePackConverterTest extends org.scalatest.FunSuite {
  val converter = new ResourcePackConverter
  println(converter.vanilla.assets.flatMap(_.blockStates.map(_.name)).mkString("\n"))
}
