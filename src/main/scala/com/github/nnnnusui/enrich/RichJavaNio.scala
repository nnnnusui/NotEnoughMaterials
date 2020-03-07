package com.github.nnnnusui.enrich

import java.nio.file.{Files, Path}

import scala.collection.JavaConverters._

object RichJavaNio {
  implicit class RichPath(val path: Path){
    def getExtension: String =
      path.getFileName.toString.reverse.takeWhile(_ != '.').reverse
    def getFileNameWithoutExtension: String =
      path.getFileName.toString.takeWhile(_ != '.')
    def mkString(sep: String): String =
      path.iterator().asScala.mkString(sep)
  }
  object RichFiles{
    def list(path: Path): Seq[Path] =
      if(Files.exists(path)) Files.list(path).iterator().asScala.toSeq
      else                   Seq.empty[Path]
    def lines(path: Path): Seq[String] =
      Files.lines(path).iterator().asScala.toSeq
  }
}
