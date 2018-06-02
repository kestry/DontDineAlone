package com.hu.tyler.dontdinealone.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

// Simple Unit Tests to learn junit
public class PrimitiveArrayServiceTest {

    @Test(expected = Exception.class)
    public void testMakeBooleanArrayFromList_givenNullList_throwException() {
        PrimitiveArrayService.makeBooleanArrayFromList(null);
    }

    @Test
    public void testMakeBooleanArrayFromList_givenValidList_getNewBooleanArrayCopyOfList() {
        List<Boolean> validListInput;
        boolean[] expectedArray;
        boolean[] actualArray;

        validListInput = new ArrayList<>();
        validListInput.add(true);
        validListInput.add(true);
        validListInput.add(false);

        expectedArray = new boolean[3];
        expectedArray[0] = true;
        expectedArray[1] = true;
        expectedArray[2] = false;

        actualArray = PrimitiveArrayService.makeBooleanArrayFromList(validListInput);

        assertArrayEquals(expectedArray, actualArray);
    }
}