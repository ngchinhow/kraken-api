package io.github.ngchinhow.kraken.websockets.utils;

import java.math.BigInteger;
import java.util.Random;

public class RandomUtils {

    public static BigInteger nextBigInteger(BigInteger endExclusive) {
        Random rand = new Random();
        BigInteger randomNumber;
        do {
            randomNumber = new BigInteger(endExclusive.bitLength(), rand);
        } while (randomNumber.compareTo(endExclusive) >= 0);

        return randomNumber;
    }
}
