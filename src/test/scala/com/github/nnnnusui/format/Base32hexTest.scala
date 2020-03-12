package com.github.nnnnusui.format

import java.nio.charset.StandardCharsets

import org.scalatest.flatspec.AnyFlatSpec

class Base32hexTest extends AnyFlatSpec {
  "decode" should "matches encode result" in {
    val src = "test Text."
    val encoded = Base32hex.encode(src.getBytes(StandardCharsets.UTF_8))
    println(encoded)
    val decoded = Base32hex.decode(encoded)
    val decodeStr = new String(decoded, StandardCharsets.UTF_8)
    println(decodeStr)
    assert(src == decodeStr)
  }
}
