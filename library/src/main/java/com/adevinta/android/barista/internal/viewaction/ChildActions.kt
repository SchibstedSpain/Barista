package com.adevinta.android.barista.internal.viewaction

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.util.HumanReadables
import com.adevinta.android.barista.internal.failurehandler.description
import com.adevinta.android.barista.internal.util.ViewTreeAnalyzer
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

object ChildActions {

  @JvmStatic
  fun onChildWithId(@IdRes childId: Int, viewAction: ViewAction): ViewAction {
    return onChildWithMatcher(withId(childId), viewAction)
  }

  @JvmStatic
  fun onChildWithMatcher(childMatcher: Matcher<View>, viewAction: ViewAction): ViewAction {
    return object : ViewAction {
      override fun getConstraints(): Matcher<View> {
        return allOf<View>(isDisplayed(), hasDescendant(childMatcher))
      }

      override fun getDescription(): String {
        return "Perform " + viewAction.description + " on a child view " + childMatcher.description()
      }

      override fun perform(uiController: UiController, view: View) {
        var foundTarget = false

        for (child in ViewTreeAnalyzer.getAllChildren(view)) {
          if (childMatcher.matches(child)) {
            foundTarget = true
            viewAction.perform(uiController, child)
            break
          }
        }

        if (!foundTarget) {
          throw PerformException.Builder()
            .withActionDescription(description)
            .withViewDescription(HumanReadables.describe(view))
            .withCause(IllegalArgumentException("Didn't find any view " + childMatcher.description()))
            .build()
        }
      }
    }
  }
}
