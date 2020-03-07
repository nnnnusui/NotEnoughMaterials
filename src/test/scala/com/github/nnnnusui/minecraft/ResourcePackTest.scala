package com.github.nnnnusui.minecraft

import java.nio.file.Paths

class ResourcePackTest extends org.scalatest.FunSuite {
  val vanillaPath = Paths.get("resource", "source", "vanilla", "1.15.2")
  val packPath = Paths.get("resource", "source", "cocricot")
  val vanilla = ResourcePack(vanillaPath)
  val pack = ResourcePack(packPath)
  val patched = vanilla.updated(pack)
  printInfo(vanilla.diet)
  printInfo(pack)
  printInfo(patched)
  printInfo(patched.diet)

  def printInfo(pack: ResourcePack): Unit =
    println(s"${pack.name}: ${pack.assets.head.blockStates.find(_.name == "acacia_leaves")}")
}
