package com.vkochenkov.equationsolver

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vkochenkov.equationsolver.activities.MainActivity
import org.hamcrest.Matchers

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

//todo
@RunWith(AndroidJUnit4::class)
class InstrumentedTests {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun checkInputAllFields() {
        //when
        Espresso.onView(withId(R.id.btnOne)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btnTwo)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btnThree)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btnFour)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btnFive)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btnSix)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btnSeven)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btnEight)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btnNine)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btnZero)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btnPoint)).perform(ViewActions.click())
        //then
        Espresso.onView(Matchers.allOf(withId(R.id.tvMain), ViewMatchers.withText("1234567890.")))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}