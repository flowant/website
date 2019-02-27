package org.flowant.website.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testIsPowerOfTwo() {
        assertFalse(NumberUtil.isPowerOfTwo(0));
        assertTrue(NumberUtil.isPowerOfTwo(1));
        assertTrue(NumberUtil.isPowerOfTwo(2));
        assertTrue(NumberUtil.isPowerOfTwo(4));
        assertTrue(NumberUtil.isPowerOfTwo(Long.MIN_VALUE));
        assertFalse(NumberUtil.isPowerOfTwo(Long.MAX_VALUE));
    }

}
