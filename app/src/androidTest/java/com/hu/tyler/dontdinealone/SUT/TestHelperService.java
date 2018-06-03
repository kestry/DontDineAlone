package com.hu.tyler.dontdinealone.SUT;

import org.junit.Ignore;

import static junit.framework.Assert.assertTrue;

@Ignore
public abstract class TestHelperService {
    public static void assertStringEquals(String label, String expected, String actual) {
        // Checks if Entity has changed values
        assertTrue(label + " : Expected(" + expected +
                        "), Actual( " + actual + ")",
                actual.equals(expected));
    }
}
