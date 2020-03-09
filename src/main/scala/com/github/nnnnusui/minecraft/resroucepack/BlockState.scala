package com.github.nnnnusui.minecraft.resroucepack

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}

import com.github.nnnnusui.format.Json

case class BlockState(name: String, json: Json.Object){
  val states: Map[String, Json.Value] =
    json.get("variants")
      .toSeq
      .flatMap(_.option[Json.Object])
      .flatMap(_.value)
      .toMap

  def writeTo(directory: Path): Unit = {
    val jsonPath = directory.resolve(s"$name.json")
    Files.createDirectories(jsonPath.getParent)
    Files.write(jsonPath, Json.stringify(json).getBytes(StandardCharsets.UTF_8))
  }
}