package com.hu.tyler.dontdinealone.general;

import org.junit.Ignore;

import static junit.framework.Assert.assertTrue;

/**
 * General test helper functions.
 * We ignore since do want to explicitly call these rather than have the test runner auto run them.
 * General tests should be put in their own class and called such.
 */
@Ignore
public abstract class TestHelperService {

    @Ignore
    public static void assertStringEquals(String label, String expected, String actual) {
        // Checks if Entity has changed values
        assertTrue(label + " : Expected(" + expected +
                        "), Actual(" + actual + ")",
                actual.equals(expected));
    }
}
