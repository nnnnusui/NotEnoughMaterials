package com.github.nnnnusui.minecraft.nem

import java.nio.file.{Path, Paths}

import com.github.nnnnusui.enrich.RichJavaNio._
import com.github.nnnnusui.format.Json
import com.github.nnnnusui.minecraft.ResourcePack
import com.github.nnnnusui.minecraft.resroucepack.{Asset, BlockState, Model, Texture}

object ResourcePackConverter {
  def convertToNem(pack: ResourcePack): ResourcePack =
    ResourcePack(
        name   = "NemTest"
      , assets = {
          val (states, models, textures) = pack.assets.map{ asset=>
            val destination = Paths.get(pack.name, asset.name)
            Paths.get(pack.name, s"${asset.name}")
            ( toMaterialJsonStates(asset.blockStates, destination)
            , asset.models   map (model    => convertModelPath(  model  , destination))
            , asset.textures map (texture  => convertTexturePath(texture, destination)))
          }.foldLeft((Map.empty[String, Json.Value], Seq.empty[Model], Seq.empty[Texture])){
            case (before, (states, models, textures)) =>
              (before._1 ++ states, before._2 ++ models, before._3 ++ textures )
          }
          Seq(Asset("nem", Seq(BlockState.fromStates("material", states)), models, textures))
        }
    )

  private def toMaterialJsonStates(blockStates: Seq[BlockState], destination: Path): Map[String, Json.Value] = {
    def searchAndConvert(value: Json.Value): Json.Value = value match {
      case Json.Object(value) =>
        value.get("model")
          .flatMap(_.option[Json.String])
          .map(it => Json.Object(value.updated("model", Json.String(destination.resolve(it.value).mkString("/")))))
          .getOrElse(searchAndConvert(Json.Object(value)))
      case Json.Array(value) => Json.Array(value.map(searchAndConvert))
      case other => other
    }
    blockStates.flatMap(blockState=>
      blockState.states.map{ case (state, value) =>
        s"${destination.mkString("/")}:${blockState.name}/$state" -> // TODO: Base32 encode
        searchAndConvert(value)
      }
    ).toMap
  }
  private def convertModelPath(model: Model, destination: Path): Model = {
    def convert(json: Json.Object): Json.Object ={
      ( for{  optTextures <- json.get("textures")
              textures    <- optTextures.option[Json.Object]
        } yield textures.value.map{ case (key, value) => key ->
          value.option[Json.String]
            .map(_.value)
            .map(it=> destination.resolve(it))
            .map(it=> Json.String(it.mkString("/")))
            .getOrElse(value)
        }
      )
      .map(textures=> Json.Object(json.value.updated("textures", Json.Object(textures))))
      .getOrElse(json)
    }
    model.copy(
        path = destination.resolve(model.path).toString
      , json = convert(model.json)
    )
  }
  private def convertTexturePath(texture: Texture, destination: Path): Texture =
    texture.copy(path = destination.resolve(texture.path).mkString("/"))
}
