package com.github.nnnnusui.minecraft.nem

import java.nio.file.Paths

import com.github.nnnnusui.minecraft.ResourcePack

class ResourcePackConverter {
  private val resourceRoot = Paths.get("resource")
  private val sourceRoot = resourceRoot.resolve("source")
  private val resultRoot = resourceRoot.resolve("result")
  private val vanillaRoot = sourceRoot.resolve("vanilla") // TODO: Read from .minecraft directory.

  val vanilla: ResourcePack = ResourcePack(vanillaRoot.resolve("1.15.2"))
}