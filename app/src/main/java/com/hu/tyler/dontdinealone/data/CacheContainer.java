package com.hu.tyler.dontdinealone.data;

// Not public because we only want caches to be used privately in Repo.
interface CacheContainer {
    ProfileCache myProfileCache = new ProfileCache();
    PreferenceCache myPreferenceCache = new PreferenceCache();
}

