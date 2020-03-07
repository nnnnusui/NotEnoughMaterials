package com.github.nnnnusui.minecraft

import java.nio.file.{Files, Path, Paths}

import com.github.nnnnusui.enrich.RichJavaNio._
import com.github.nnnnusui.format.Json

case class ResourcePack(name: String, assets: Seq[ResourcePack.Asset])
object ResourcePack{
  def apply(path: Path): ResourcePack ={
    val name =
      if(Files.isDirectory(path)) path.getFileName.toString
      else                        path.getFileNameWithoutExtension
    new ResourcePack(name, getAssets(path))
  }

  case class Asset(name: String, blockStates: Seq[BlockState], models: Seq[Model], textures: Seq[Texture])
  case class BlockState(name: String, json: Json.Object){
    val states: Map[String, Json.Value] =
      json.get("variants")
      .toSeq
      .flatMap(_.option[Json.Object])
      .flatMap(_.value)
      .toMap
  }
  case class Model(  path: Path, json: Json.Object)
  case class Texture(path: Path, data: Array[Byte])

  def getAssets(resourcePackPath: Path): Seq[Asset] ={
    RichFiles.list(resourcePackPath.resolve("assets"))
      .filter(path=> Files.isDirectory(path))
      .map{   dir =>
        val blockStates = getBlockStates( dir)
        val models      = getModels(      dir, blockStates)
        val textures    = getTextures(    dir, models)
        Asset(dir.getFileNameWithoutExtension, blockStates, models, textures)
      }
  }
  def getBlockStates(assetPath: Path): Seq[BlockState] ={
    RichFiles.list(assetPath.resolve("blockstates"))
      .filter( path => path.getExtension == "json")
      .flatMap(path => Json.parse(RichFiles.lines(path).mkString) match {
        case Right(it: Json.Object)=> Some(BlockState(path.getFileNameWithoutExtension, it))
        case it                    => println(s"WARNING: $it"); None
      })
  }
  def getModels(assetPath: Path, blockStates: Seq[BlockState]): Seq[Model] ={
    val modelsDir = assetPath.resolve("models")
    blockStates
      .flatMap(_.json.value.get("variants"))
      .flatMap(_.option[Json.Object])
      .flatMap(_.value.values)
      .flatMap(_ match {
        case obj: Json.Object =>
          obj.get("model")
            .flatMap(_.option[Json.String])
            .map(_.value)
            .toSeq
        case array: Json.Array =>
          array.value
            .flatMap(_.option[Json.Object])
            .flatMap(_.value.get("model"))
            .flatMap(_.option[Json.String])
            .map(_.value)
      })
      .distinct
      .map(it=> Paths.get(s"$it.json"))
      .flatMap(localPath=>
        Json.parse(RichFiles.lines(modelsDir.resolve(localPath)).mkString)
          .toSeq
          .flatMap(_.option[Json.Object])
          .map(json => Model(localPath, json))
      )
  }
  def getTextures(assetPath: Path, models: Seq[Model]): Seq[Texture] ={
    val texturesDir = assetPath.resolve("textures")
    models.flatMap(model=>
      model.json.get("textures")
      .toSeq
      .flatMap(_.option[Json.Object])
      .flatMap(_.value.values)
      .flatMap(_.option[Json.String])
      .map(_.value)
      .distinct
      .map(it=> Paths.get(s"$it.png"))
      .map(localPath=>
        Texture(localPath, Files.readAllBytes(texturesDir.resolve(localPath)))
      )
    )
  }
}