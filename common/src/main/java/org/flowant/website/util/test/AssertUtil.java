package org.flowant.website.util.test;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Assert;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AssertUtil {
    public static <T> void assertListEquals(List<T> expected, List<T> actual) {
        if (expected == null)
            Assert.assertTrue(actual == null || actual.size() == 0);
        else if (expected.size() == 0)
            Assert.assertTrue(actual == null || actual.size() == 0);
        else
            Assert.assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    public static <T> T assertEquals(T expected, T actual) {
        Assert.assertNotNull(expected);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.getClass(), actual.getClass());

        Method[] methods = expected.getClass().getDeclaredMethods();

        for (Method method : methods) {
            String name = method.getName();

            if (name.startsWith("get") && !name.startsWith("getCru")
                    && !name.startsWith("getReputation")) {
                log.trace("assertEquals: method name:{}", name);
                try {
                    Class<?> cls = method.getReturnType();
                    if (List.class.isAssignableFrom(cls)) {
                        assertListEquals((List<Object>) method.invoke(expected),
                                (List<Object>) method.invoke(actual));
                    } else {
                        Assert.assertEquals(method.invoke(expected), method.invoke(actual));
                    }
                } catch (Exception e) {
                    log.error(ExceptionUtils.getStackTrace(e));
                }
            }
        }
        return actual;
    }

}
