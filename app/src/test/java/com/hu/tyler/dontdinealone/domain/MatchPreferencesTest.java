package com.hu.tyler.dontdinealone.domain;

import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.model.MatchPreferences;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

// Test: Mackenzie Huynh
public class MatchPreferencesTest {

    MatchPreferences preferences = new MatchPreferences();

    @Test(expected = Exception.class)
    public void testHasMatch_WithNullList_ThrowException() {
        preferences.hasMatch(null);
    }

    @Test(expected = Exception.class)
    public void testConformPreferences_WithNullList_ThrowException() {
        preferences.conformPreferences(null);
    }

    @Test(expected = Exception.class)
    public void testCopy_WithNullList_ThrowException() {
        preferences.copy(null);
    }
}
