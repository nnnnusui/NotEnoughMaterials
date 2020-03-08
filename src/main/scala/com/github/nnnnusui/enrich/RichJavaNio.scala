package com.github.nnnnusui.enrich

import java.net.URI
import java.nio.file.{FileSystem, FileSystems, Files, Path, Paths}

import scala.collection.JavaConverters._

object RichJavaNio {
  implicit class RichPath(val src: Path){
    def extension: String =
      src.fileName.toString match {
        case it if it.contains('.')=> it.split('.').reverse.head
        case _                     => ""
      }
    def fileNameWithoutExtension: String =
      src.fileName.toString match {
        case it if it.contains('.')=> it.split('.').reverse.tail.reverse.mkString(".")
        case it                    => it
      }
    def toSeq: Seq[Path] = src.iterator().asScala.toSeq
    def head: Path      = src.toSeq.head
    def tail: Seq[Path] = src.toSeq.tail
    def mkString(sep: String): String = src.toSeq.mkString(sep)

    def fileName: Path ={
      val str = src.getFileName.toString
      if(str.reverse.head == '/') Paths.get(str.dropRight(1))
      else                        src.getFileName
    }
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
  object RichFileSystems{
    def newFileSystem(uri: URI, options: Map[String, Any], loader: ClassLoader): FileSystem =
      FileSystems.newFileSystem(uri, options.asJava, loader)
  }
}
