package org.flowant.website.util;

public class NumberUtil {

    public static boolean isPowerOfTwo (long x) {
        return x != 0 && ((x & (x-1)) == 0);
    }

}
