package com.hu.tyler.dontdinealone.domain;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.hu.tyler.dontdinealone.MyApp;
import com.hu.tyler.dontdinealone.data.Entity;

public abstract class ViewService {
    /**
     * @return "[package]:id/[xml-id]"
     * where [package] is your package and [xml-id] is id of view
     * or "no-id" if there is no id
     * (from stack overflow)
     */
    public static String getViewName(View view) {
        return view.getResources().getResourceName(view.getId());
    }

    /**
     * Returns a view from the viewName, which is a string in the form of "[package]:id/[xml-id]".
     */
    public static View getView(String viewName, Activity activity) {
        int viewId = activity.getResources().getIdentifier(Entity.user.getAvatarViewName(),
                "ImageView", MyApp.getContext().getPackageName());
        return activity.findViewById(viewId);
    }
}
