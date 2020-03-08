package com.github.nnnnusui.minecraft

import java.nio.file.Paths

class ResourcePackTest extends org.scalatest.FunSuite {
  val vanillaPath = Paths.get("resource", "source", "vanilla", "1.15.2")
  val vanillaJarPath = Paths.get("resource", "source", "vanilla", "1.15.2.jar")
  val vanilla = ResourcePack(vanillaPath)
  val fromJar = ResourcePack(vanillaJarPath)
  println(vanilla == fromJar) // not equal ...
  printInfo(vanilla)
  printInfo(fromJar)

  def printInfo(pack: ResourcePack): Unit =
    println(s"${pack.name}: ${pack.assets.head.models.length}")
}
