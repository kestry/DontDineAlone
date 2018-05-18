package com.hu.tyler.dontdinealone.data;

import java.util.HashMap;

// Not public because we only want caches to be used privately in Repo.
class Cache extends HashMap<String, Object> {
    public void setToDefault() { }
}
