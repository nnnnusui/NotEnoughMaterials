package com.github.nnnnusui
package minecraft.resroucepack

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}

import com.github.nnnnusui.format.Json

case class Model(path: String, json: Json.Object){

  def writeTo(directory: Path): Unit = {
    val jsonPath = directory.resolve(path)
    Files.createDirectories(jsonPath.getParent)
    Files.write(jsonPath, Json.stringify(json).getBytes(StandardCharsets.UTF_8))
  }

  override def equals(obj: Any): Boolean = obj match {
    case it: Model => json == it.json
    case _         => super.equals(obj)
  }
}
