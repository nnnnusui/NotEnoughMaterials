package com.github.nnnnusui.minecraft.resroucepack

import com.github.nnnnusui.format.Json

case class BlockState(name: String, json: Json.Object){
  val states: Map[String, Json.Value] =
    json.get("variants")
      .toSeq
      .flatMap(_.option[Json.Object])
      .flatMap(_.value)
      .toMap
}