package com.github.nnnnusui.format

import java.nio.ByteBuffer
import java.util

object Base32hex {
  def encode(source: Array[Byte]): String      = _encode(source)
  def decode(source: String     ): Array[Byte] = _decode(source)

  private val bitPerByte =  8
  private val radix      = 32
  private val blockSize  = 40 // bit per base32 block
  private val wordSize   =  5 // bit per base32 word
  private val fillCharacter = '='


  private def _encode(_source: Array[Byte]): String ={
    val sourceBitQuantity  = _source.length * bitPerByte
    val resultWordQuantity = ceil(sourceBitQuantity, from = wordSize)
    val resultBitQuantity  = resultWordQuantity * wordSize
    val fillCharacterQuantity = ceil(resultBitQuantity - sourceBitQuantity, from = wordSize)
    val source = util.BitSet.valueOf(_source).reverse(bitPerByte)

    Range(0, resultWordQuantity)
        .map(index=> source.get(index * wordSize, (index +1) * wordSize))
        .map(getWord)
        .map(it=> Integer.toString(it, radix))
        .mkString
        .toUpperCase() +
      fillCharacter.toString * fillCharacterQuantity
  }
  private def _decode(_source: String): Array[Byte] ={
    val source = _source.replace(fillCharacter.toString, "")
    val sourceBytes = ByteBuffer.allocate(source.length * wordSize)
    source.toCharArray
      .map(_.toString)
      .map(it=> BigInt(it, radix))
      .map(_.toByte)
      .foreach(sourceBytes.put)
    setWords(sourceBytes.array())
      .toByteArray
  }

  def ceil(x: Int, from: Int): Int =
    (x / from) + (if (x % from == 0) 0 else 1)
  private def getWord(bitSet: util.BitSet): Int =
    Range(0, wordSize)
    .map(bitSet.get)
    .map(it=> if(it) 1 else 0)
    .fold(0)((sum, bit)=> (sum << 1) + bit)
  private def setWords(values: Array[Byte]): util.BitSet ={
    val bitSet = new util.BitSet(values.length * wordSize)
    values.indices
      .foreach{index=>
        util.BitSet.valueOf(Array(values(index))).reverse(wordSize)
          .stream()
          .map(_ + (index * wordSize))
          .forEach(it=> bitSet.set(it))
      }
    bitSet.reverse(bitPerByte)
  }
  implicit class RichBitSet(val src: util.BitSet){
    def reverse(unitSize: Int): util.BitSet = {
      val result = new util.BitSet(src.length)
      src.stream.forEach{ it=>
        val multiplesOfUnit = (it / unitSize) * unitSize
        val lowerThanUnit   = (unitSize - 1) - (it % unitSize)
        result.set(multiplesOfUnit + lowerThanUnit)
      }
      result
    }
  }
}
