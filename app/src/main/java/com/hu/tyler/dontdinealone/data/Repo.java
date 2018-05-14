package com.hu.tyler.dontdinealone.data;

import com.hu.tyler.dontdinealone.util.Callback;

public interface Repo {
    public void setToDefault();
    public boolean isDirty();
    public Object getDocName(String key);
    public Object get(String key);
    public void set(String key, Object value);
    public void store(Callback callback);
    public void load(Callback callback);
}
