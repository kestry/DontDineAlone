package com.hu.tyler.dontdinealone.domain;

import java.util.List;

public abstract class PrimitiveArrayService {
    public static boolean[] makeBooleanArrayFromList(List<Boolean> list) {
        boolean[] array = new boolean[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            array[i] = list.get(i);
        }
        return array;
    }
}
