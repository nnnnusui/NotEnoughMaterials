package com.github.nnnnusui
package minecraft

import java.nio.file.{Files, Path}

import com.github.nnnnusui.enrich.RichJavaNio._
import com.github.nnnnusui.format.Json
import com.github.nnnnusui.minecraft.resroucepack.{Asset, BlockState, Model, Texture}

case class ResourcePack(name: String, assets: Seq[Asset]){
  def dieted: ResourcePack =
    this.copy(assets = assets.map(_.dieted))
  def updated(patch: ResourcePack): ResourcePack =
    this.copy(assets = assets.map(asset=>
      patch.assets
      .find(_.name == asset.name)
      .map(it=> asset.updated(it))
      .getOrElse(asset)
    ))
//  def complementedWith(src: ResourcePack): ResourcePack =
//    this.copy(assets = assets.map(asset=>
//      src.assets
//      .find(_.name == asset.name)
//      .map(it=> asset.complementedWith(it))
//      .getOrElse(asset)
//    ))
}
object ResourcePack{
  def apply(path: Path): ResourcePack ={
    val name =
      if(Files.isDirectory(path)) path.getFileName.toString
      else                        path.fileNameWithoutExtension
    new ResourcePack(name, getAssets(path))
  }

  def getAssets(resourcePackPath: Path): Seq[Asset] ={
    RichFiles.list(resourcePackPath.resolve("assets"))
      .filter(path=> Files.isDirectory(path))
      .map{   dir =>
        val blockStates = getBlockStates( dir)
        val models      = getModels(      dir)
        val textures    = getTextures(    dir)
        Asset(dir.fileNameWithoutExtension, blockStates, models, textures)
      }
  }
  def getBlockStates(assetPath: Path): Seq[BlockState] ={
    RichFiles.list(assetPath.resolve("blockstates"))
      .filter( path => path.extension == "json")
      .flatMap(path => Json.parse(RichFiles.lines(path).mkString) match {
        case Right(it: Json.Object)=> Some(BlockState(path.fileNameWithoutExtension, it))
        case it                    => println(s"WARNING: $it"); None
      })
  }
  def getModels(assetPath: Path): Seq[Model] ={
    val modelsDir = assetPath.resolve("models")
    RichFiles.walk(modelsDir)
    .filter(it=> !Files.isDirectory(it))
    .filter(_.extension == "json")
    .flatMap{path=>
      val localPath = modelsDir.relativize(path)
      Json.parse(RichFiles.lines(path).mkString)
      .toSeq
      .flatMap(_.option[Json.Object])
      .map(json=> Model(localPath, json))
    }
  }
  def getTextures(assetPath: Path): Seq[Texture] ={
    val texturesDir = assetPath.resolve("textures")
    RichFiles.walk(texturesDir)
    .filter(it=> !Files.isDirectory(it))
    .filter(_.extension == "png")
    .map{path=>
      val localPath = texturesDir.relativize(path)
      Texture(localPath, Files.readAllBytes(path))
    }
  }
}