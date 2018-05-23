package com.hu.tyler.dontdinealone.domain;

import java.util.Map;

/**
 * This class is abstract because it provides static service implementations without instantiation.
 * Usage:
 *    Class.method();
 */
public abstract class FillMapService {

    /**
     * Fills a map with the arrays of keys and values.
     */
    public static void fillMap(Map<String, Object> map, String[] keys, Object[] values) {
        for (int i = 0; i < keys.length; ++i) {
            map.put(keys[i], values[i]);
        }
    }

    /**
     * Fills a map with the arrays of keys and values.
     */
    public static void fillMap(Map<String, Boolean> map, String[] keys, boolean[] values) {
        for (int i = 0; i < keys.length; ++i) {
            map.put(keys[i], values[i]);
        }
    }
}
