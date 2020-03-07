package com.github.nnnnusui
package minecraft.resroucepack

import java.nio.file.Paths

import com.github.nnnnusui.format.Json

case class Asset(name: String, blockStates: Seq[BlockState], models: Seq[Model], textures: Seq[Texture]){
  def diet: Asset ={
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
      .map(it=> Paths.get(s"$it.json"))
    val texturePaths =
      models
      .flatMap(_.json.get("textures"))
      .flatMap(_.option[Json.Object])
      .flatMap(_.value.values)
      .flatMap(_.option[Json.String])
      .map(_.value)
      .distinct
      .map(it=> Paths.get(s"$it.png"))
    this.copy(
      models   = models.filter(model=> modelPaths.contains(model.path))
     ,textures = textures.filter(texture=> texturePaths.contains(texture.path))
    )
  }
}
