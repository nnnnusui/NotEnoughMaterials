package com.github.nnnnusui
package minecraft.resroucepack

import java.nio.file.{Files, Path}

case class Texture(path: String, data: Array[Byte]){

  def writeTo(directory: Path): Unit = {
    val targetPath = directory.resolve(path)
    Files.createDirectories(targetPath.getParent)
    Files.write(targetPath, data)
  }

  override def equals(obj: Any): Boolean = obj match {
    case it: Texture => data sameElements it.data
    case _           => super.equals(obj)
  }
}
