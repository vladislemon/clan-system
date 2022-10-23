package org.example.clan;

import org.junit.jupiter.api.Assertions;

import java.util.Collection;

public class TestUtils {
    public static <T> void assertContainsEqualElements(Collection<T> expected, Collection<T> actual) {
        if (expected == null || actual == null) {
            Assertions.assertEquals(expected, actual);
            return;
        }
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertTrue(expected.containsAll(actual));
        Assertions.assertTrue(actual.containsAll(expected));
    }
}
