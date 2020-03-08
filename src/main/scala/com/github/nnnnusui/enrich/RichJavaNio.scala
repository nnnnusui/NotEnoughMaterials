package com.github.nnnnusui.enrich

import java.nio.file.{Files, Path}

import scala.collection.JavaConverters._

object RichJavaNio {
  implicit class RichPath(val path: Path){
    def extension: String =
      path.getFileName.toString match {
        case it if it.contains('.')=> it.split('.').reverse.head.reverse
        case _                     => ""
      }
    def fileNameWithoutExtension: String =
      path.getFileName.toString match {
        case it if it.contains('.')=> it.split('.').reverse.tail.reverse.mkString(".")
        case it                    => it
      }
    def mkString(sep: String): String =
      path.iterator().asScala.mkString(sep)
  }
  object RichFiles{
    def list(path: Path): Seq[Path] =
      if(Files.exists(path)) Files.list(path).iterator().asScala.toSeq
      else                   Seq.empty[Path]
    def lines(path: Path): Seq[String] =
      Files.lines(path).iterator().asScala.toSeq
    def walk(path: Path): Seq[Path] =
      Files.walk(path).iterator().asScala.toSeq
  }
}
