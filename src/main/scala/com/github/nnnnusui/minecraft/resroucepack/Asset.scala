package com.github.nnnnusui
package minecraft.resroucepack

import java.nio.file.Path

import com.github.nnnnusui.format.Json

case class Asset(name: String, blockStates: Seq[BlockState], models: Seq[Model], textures: Seq[Texture]){
  def dieted: Asset ={
    val modelPaths =
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
      .map(it=> s"$it.json")
    val texturePaths =
      models
      .flatMap(_.json.get("textures"))
      .flatMap(_.option[Json.Object])
      .flatMap(_.value.values)
      .flatMap(_.option[Json.String])
      .map(_.value)
      .distinct
      .map(it=> s"$it.png")
    this.copy(
      models   = models  .filter(model  => modelPaths  .contains(model.path  ))
     ,textures = textures.filter(texture=> texturePaths.contains(texture.path))
    )
  }

  def updated(patch: Asset): Asset =
    this.copy(
      blockStates = blockStates.map(it=>
        patch.blockStates
        .find(_.name == it.name)
        .getOrElse(it)
      )
     ,models      = models.map(it=>
        patch.models
        .find(_.path == it.path)
        .getOrElse(it)
      )
     ,textures    = textures.map(it=>
        patch.textures
        .find(_.path == it.path)
        .getOrElse(it)
      )
    )
//  def complementedWith(src: Asset): Asset ={
//
//  }

  def writeTo(directory: Path): Unit ={
    val assetPath = directory.resolve(name)
    blockStates.foreach(_.writeTo(assetPath resolve "blockstates"))
    models     .foreach(_.writeTo(assetPath resolve "models"     ))
    textures   .foreach(_.writeTo(assetPath resolve "textures"   ))
  }
}
