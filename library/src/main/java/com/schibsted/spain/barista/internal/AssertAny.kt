package com.schibsted.spain.barista.internal

import android.support.test.espresso.AmbiguousViewMatcherException
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.view.View
import com.schibsted.spain.barista.internal.failurehandler.SpyFailureHandler
import com.schibsted.spain.barista.internal.failurehandler.description
import com.schibsted.spain.barista.internal.matcher.BooleanMatcher
import com.schibsted.spain.barista.internal.matcher.HelperMatchers.firstViewOf
import com.schibsted.spain.barista.internal.util.resourceMatcher
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

/**
 * Extension function alias for [assertAnyView]
 */
fun Matcher<View>.assertAny(condition: Matcher<View>) {
    assertAnyView(viewMatcher = this, condition = condition)
}

/**
 * Extension function alias for [assertAnyView]
 */
inline fun <reified T: View> assertAny(resId: Int, noinline block: (T) -> Boolean) {
    assertAny(resId.resourceMatcher(), block)
}

/**
 * Extension function alias for [assertAnyView]
 */
inline fun <reified T: View> assertAny(text: String, noinline block: (T) -> Boolean) {
    assertAny(withText(text), block)
}

/**
 * Extension function alias for [assertAnyView]
 */
inline fun <reified T: View> assertAny(matcher: Matcher<View>, noinline block: (T) -> Boolean) {
    assertAnyView(viewMatcher = matcher, condition = BooleanMatcher(block) as Matcher<View>)
}

/**
 * Performs an assertion of a [condition] on a view described by [viewMatcher].
 *
 * Attempts to assert using multiple scenarios for the [viewMatcher]:
 * 1. Just one view matches the [viewMatcher].
 * 2. Multiple views match the [viewMatcher]: will pass if at least one of them matches the [condition].
 */
fun assertAnyView(viewMatcher: Matcher<View>, condition: Matcher<View>) {
    val spyFailureHandler = SpyFailureHandler()
    try {
        tryToAssert(viewMatcher, condition, spyFailureHandler)
    } catch (multipleViewsError: AmbiguousViewMatcherException) {
        try {
            tryToAssertFirstView(viewMatcher, condition, spyFailureHandler)
        } catch (noneMatchedError: Throwable) {
            spyFailureHandler.resendFirstError(
                "None of the views matching (${viewMatcher.description()}) did match the condition (${condition.description()})")
        }
    } catch (singleViewNotFoundError: NoMatchingViewException) {
        spyFailureHandler.resendFirstError("No view matching (${viewMatcher.description()}) was found")
    } catch (singleViewMatchError: Throwable) {
        spyFailureHandler.resendFirstError("View (${viewMatcher.description()}) didn't match condition (${condition.description()})")
    }
}

private fun tryToAssertFirstView(viewMatcher: Matcher<View>, condition: Matcher<View>, spyFailureHandler: SpyFailureHandler) {
    onView(firstViewOf(allOf(viewMatcher, condition)))
            .withFailureHandler(spyFailureHandler)
            .check(ViewAssertions.matches(condition))
}

private fun tryToAssert(viewMatcher: Matcher<View>, condition: Matcher<View>, spyFailureHandler: SpyFailureHandler) {
    onView(viewMatcher)
            .withFailureHandler(spyFailureHandler)
            .check(ViewAssertions.matches(condition))
}
