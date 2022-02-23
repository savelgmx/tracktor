package com.elegion.tracktor.ui.map;

import android.support.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.elegion.tracktor.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4ClassRunner.class)
public class CounterFragmentTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void onStartClick() {
        //VERIFY that start button is pressed
        onView(withId(R.id.buttonStart)).perform(click());
    }

    @Test
    public void onStopClick() {
    }
}