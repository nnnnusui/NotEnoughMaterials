package com.github.nnnnusui.enrich

import java.nio.file.{Files, Path}

object RichJavaNio {
  implicit class RichPath(val path: Path){
    def getExtension: String =
      path.getFileName.toString.reverse.takeWhile(_ != '.').reverse
    def getFileNameWithoutExtension: String =
      path.getFileName.toString.takeWhile(_ != '.')
  }
  object RichFiles{
    import scala.collection.JavaConverters._
    def list(path: Path): Seq[Path] =
      if(Files.exists(path)) Files.list(path).iterator().asScala.toSeq
      else                   Seq.empty[Path]
    def lines(path: Path): Seq[String] =
      Files.lines(path).iterator().asScala.toSeq
  }
}
