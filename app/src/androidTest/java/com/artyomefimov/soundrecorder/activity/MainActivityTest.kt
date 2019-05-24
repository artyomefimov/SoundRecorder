package com.artyomefimov.soundrecorder.activity

import android.content.ComponentName
import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.services.recordservice.RecordService
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class MainActivityTest {

    @Rule
    @JvmField
    var rule = ActivityTestRule(MainActivity::class.java)

//    @Rule
//    @JvmField
//    var intentRule = IntentsTestRule<MainActivity>(MainActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @Test
    fun tapOnFilesBottomNavigationItemOpenFilesFragment() {
        onView(withId(R.id.action_files))
            .perform(click())

        onView(withId(R.id.files_list))
            .check(matches(isDisplayed()))
        onView(withId(R.id.folder_path_view))
            .check(doesNotExist())
    }

    @Test
    fun tapOnRecordBottomNavigationItemOpenRecordFragment() {
        onView(withId(R.id.action_files))
            .perform(click())
        onView(withId(R.id.action_record))
            .perform(click())

        onView(withId(R.id.folder_path_view))
            .check(matches(isDisplayed()))
        onView(withId(R.id.files_list))
            .check(doesNotExist())
    }

    @Test
    fun tapOnFolderPathViewOpensFileChooser() {
        onView(withId(R.id.folder_path_view))
            .perform(click())

        onView(withText(R.string.picker_overview_heading))
            .check(matches(isDisplayed()))
    }

    @Test
    fun tapOnRecordButtonBlocksFileChooser() {
        onView(withId(R.id.record_button))
            .perform(click())
        onView(withId(R.id.folder_path_view))
            .perform(click())

        onView(withText(R.string.picker_internal_storage_text))
            .check(doesNotExist())

        onView(withId(R.id.record_button))
            .perform(click())
    }
}