package com.github.nnnnusui.minecraft

import java.nio.file.{Files, Path}

import com.github.nnnnusui.enrich.RichJavaNio._
import com.github.nnnnusui.format.Json

class ResourcePack(val name: String, val assets: Seq[ResourcePack.Asset])
object ResourcePack{
  def apply(path: Path): ResourcePack = new ResourcePack(path.getFileNameWithoutExtension, getAssets(path))

  class Asset(val name: String, val blockStates: Seq[BlockState])
  class BlockState(val name: String, val json: Json)
  class Model
  class Texture

  def getAssets(resourcepackPath: Path): Seq[Asset] ={
    RichFiles.list(resourcepackPath.resolve("assets"))
      .filter(path=> Files.isDirectory(path))
      .map(   dir => new Asset(dir.getFileNameWithoutExtension, getBlockStates(dir)))
  }
  def getBlockStates(assetPath: Path): Seq[BlockState] ={
    RichFiles.list(assetPath.resolve("blockstates"))
      .filter(path => path.getExtension == "json")
      .map(   path => Json.parse(RichFiles.lines(path).mkString) match {
        case Right(it)=> Some(new BlockState(path.getFileNameWithoutExtension, it))
        case Left( it)=> println(s"WARNING: $it"); None
      }).filter(_.isDefined).map(_.get)
  }
}