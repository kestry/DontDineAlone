package com.hu.tyler.dontdinealone.domain;

import static org.mockito.Mockito.*;

import org.mockito.Mock;

import android.app.Activity;
import android.view.View;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Simple unit tests to learn Mockito.
 */
public class ViewServiceTest {

    private static final String viewName = "viewName";

    @Mock
    Activity mockActivity = mock(Activity.class);

    @Mock
    View mockView = mock(View.class);

    @Test(expected = Exception.class)
    public void testWhenGettingViewName_WithNullView_ThrowException() {
        ViewService.getViewName(null);
    }

    @Test(expected = Exception.class)
    public void testWhenGettingView_WithValidNameAndNullActivity_ThrowException() {
        ViewService.getView(viewName, null);
    }

    @Test(expected = Exception.class)
    public void testWhenGettingView_WithNullViewNameAndValidActivity_ThrowException() {
        ViewService.getView(null, mockActivity);
    }
}