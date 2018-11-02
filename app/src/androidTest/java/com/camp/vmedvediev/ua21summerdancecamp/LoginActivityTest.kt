package com.camp.vmedvediev.ua21summerdancecamp

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ua.dancecamp.vmedvediev.ua21summerdancecamp.R
import ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.MainActivity
import ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.login.LoginActivity

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val testRule: ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun init() {
        Intents.init()
    }

    @Test
    fun testSuccessfulLogin() {
        Espresso.onView(withId(R.id.passwordTextInputEditText)).perform(ViewActions.typeText("12345678"))
        Espresso.onView(withId(R.id.cancelButton)).perform(ViewActions.scrollTo())
        Espresso.onView(withId(R.id.confirmPasswordTextInputEditText)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.confirmPasswordTextInputEditText)).perform(ViewActions.typeText("12345678"))
        Espresso.onView(withId(R.id.nextButton)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun testSmallPassword() {
        Espresso.onView(withId(R.id.passwordTextInputEditText)).perform(ViewActions.typeText("1234"))
        Espresso.onView(withId(R.id.cancelButton)).perform(ViewActions.scrollTo())
        Espresso.onView(withId(R.id.nextButton)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun testPasswordsDontMatch() {
        Espresso.onView(withId(R.id.passwordTextInputEditText)).perform(ViewActions.typeText("12345678"))
        Espresso.onView(withId(R.id.cancelButton)).perform(ViewActions.scrollTo())
        Espresso.onView(withId(R.id.confirmPasswordTextInputEditText)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.confirmPasswordTextInputEditText)).perform(ViewActions.typeText("123456"))
        Espresso.onView(withId(R.id.nextButton)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
    }
}