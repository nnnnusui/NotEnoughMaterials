package com.github.nnnnusui.minecraft.nem

import java.nio.file.Paths

import com.github.nnnnusui.minecraft.ResourcePack
import org.scalatest.flatspec.AnyFlatSpec

class ResourcePackConverterTest extends AnyFlatSpec {
  private val resourceRoot = Paths.get("resource")
  private val sourceRoot = resourceRoot resolve "source"
  private val targetRoot = resourceRoot resolve "target"

  "test" should "" in {
    val packName = "1.15.2"
    val from = sourceRoot resolve "vanilla" resolve packName
    val pack = ResourcePack(from)
    val converted = ResourcePackConverter.convertToNem(pack)
    converted.writeTo(targetRoot)
  }

//  val dotMinecraft: Path = Paths.get("G:\\game\\Minecraft\\.minecraft")
//  val versions: Path = dotMinecraft.resolve("versions")
//  val versionList: Map[String, Path] =
//    RichFiles.list(versions)
//    .map(it=> it.resolve(s"${it.fileName}.jar"))
//    .filter(it=> Files.exists(it))
//    .map(it=> (s"${it.fileNameWithoutExtension}", it))
//    .toMap
}
