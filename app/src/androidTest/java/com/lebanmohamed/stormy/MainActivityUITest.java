package com.lebanmohamed.stormy;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.lebanmohamed.stormy.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class MainActivityUITest
{
    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void CurrentDataNotNull() throws Exception
    {
        onView(withId(R.id.temperatureValue)).check(matches(notNullValue()));
    }

    @Test
    public void DailyDataNotNull() throws Exception
    {
        onView(withId(R.id.dailyForecastButton)).perform(click());


        onView(withId(R.id.dailyListItems)).check(matches(not(null)));
    }

    @Test
    public void  HourlyDataNotNull() throws Exception
    {
        onView(withId(R.id.hourlyForecastButton)).perform(click());

        onView(withId(R.id.hourlyListItems)).check(matches(notNullValue()));
    }



}
