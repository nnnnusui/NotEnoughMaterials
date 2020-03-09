package com.github.nnnnusui.minecraft

import java.nio.file.{Files, Paths}

import org.scalatest.flatspec.AnyFlatSpec

class ResourcePackTest extends AnyFlatSpec {
  private val resourceRoot = Paths.get("resource")
  private val sourceRoot = resourceRoot resolve "source"
  private val targetRoot = resourceRoot resolve "target"

  "writeTo" should "match source" in {
    val packName = "1.15.2"
    val from = sourceRoot resolve "vanilla" resolve s"$packName.jar"
    val to   = targetRoot resolve packName
    val pack = ResourcePack(from)
    if(Files.notExists(to))
      pack.writeTo(to.getParent)
    val writtenPack = ResourcePack(to)
    assert(pack == writtenPack)
  }
}
