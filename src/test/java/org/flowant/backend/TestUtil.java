package org.flowant.backend;

import java.util.List;

import org.junit.Assert;

public class TestUtil {
    public static <T> void assertListEquals(List<T> expected, List<T> actual) {
        if (expected == null)
            Assert.assertTrue(actual == null || actual.size() == 0);
        else if (expected.size() == 0)
            Assert.assertTrue(actual == null || actual.size() == 0);
        else
            Assert.assertEquals(expected, actual);
    }
}
