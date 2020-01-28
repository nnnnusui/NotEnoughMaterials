package com.github.nnnnusui.format;

import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Base32hex {
    public static String encode(byte[] source) { return instance._encode(source); }
    public static byte[] decode(String source) { return instance._decode(source); }
    //    public static Base32hex getInstance() { return instance; }
    private static Base32hex instance = new Base32hex();
    private Base32hex() {}

    private final int radix = 32;
    private final int blockSize = 40; // bit per base32 block
    private final int wordSize  =  5; // bit per base32 word
    private final char fillCharacter = '=';
    private final int bitPerByte  = 8;

    public String _encode(byte[] _source){
        final int sourceBitQuantity = _source.length * bitPerByte;
        final int resultWordQuantity = ((sourceBitQuantity -1) / wordSize) +1;
        final int resultBitQuantity = ((sourceBitQuantity -1) / blockSize +1) * blockSize;
        final BitSet source = reverse(BitSet.valueOf(_source), bitPerByte);

        return IntStream.range(0, resultWordQuantity)
                .mapToObj(index-> source.get(index * wordSize, (index +1) * wordSize))
                .map(this::getWord)
                .map(it-> Integer.toString(it, radix))
                .map(String::valueOf)
                .collect(Collectors.joining())
                .toUpperCase()
            + Stream.generate(() -> Character.toString(fillCharacter))
                .limit((resultBitQuantity - sourceBitQuantity) / wordSize)
                .collect(Collectors.joining());
    }
    public byte[] _decode(String _source){
        final String source = _source.replace(Character.toString(fillCharacter), "");
        final ByteBuffer sourceBytes = ByteBuffer.allocate(source.length() * wordSize);
        source.chars()
                .mapToObj(it -> (char) it)
                .map(it -> Character.toString(it))
                .map(it -> Byte.parseByte(it, radix))
                .forEach(sourceBytes::put);

        final BitSet result = setWords(sourceBytes.array());
        return result.toByteArray();
    }

    private int getWord(BitSet bitSet){
        return IntStream.range(0, wordSize)
                .mapToObj(bitSet::get)
                .map(it-> it ? 1 : 0)
                .reduce(0, (sum, bit) -> (sum << 1) + bit); // Binary to decimal.
    }
    private BitSet setWords(byte[] values){ // TODO: Refactoring
        final BitSet bitSet = new BitSet(values.length * wordSize);
        IntStream.range(0, values.length)
                .forEach(index->
                    reverse(BitSet.valueOf(new byte[]{ values[index] }), wordSize)
                            .stream()
                            .map(it-> it + (index * wordSize))
                            .forEach(bitSet::set)
                );
        return reverse(bitSet, bitPerByte);
    }

    private BitSet reverse(BitSet source, int unitSize){
        final BitSet result = new BitSet(source.length());
        source.stream()
                .forEach(it-> {
                    int multiplesOfUnit = (it / unitSize) * unitSize;
                    int lowerThanUnit = (unitSize -1) - (it % unitSize);
                    result.set(multiplesOfUnit + lowerThanUnit);
                });
        return result;
    }
}
