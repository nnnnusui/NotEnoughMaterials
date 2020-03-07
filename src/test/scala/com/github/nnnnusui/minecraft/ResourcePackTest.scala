package com.github.nnnnusui.minecraft

import java.nio.file.Paths

class ResourcePackTest extends org.scalatest.FunSuite {
  val vanillaPath = Paths.get("resource", "source", "vanilla", "1.15.2")
  val pack = ResourcePack(vanillaPath)
  val dieted = pack.diet
  println(pack.assets.head.textures.length)
  println(dieted.assets.head.textures.length)
}
