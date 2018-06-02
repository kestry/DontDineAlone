package com.hu.tyler.dontdinealone.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.hu.tyler.dontdinealone.LobbyActivity;

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