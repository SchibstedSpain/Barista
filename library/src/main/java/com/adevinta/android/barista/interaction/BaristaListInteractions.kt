package com.adevinta.android.barista.interaction

import android.content.Context
import android.view.View
import android.widget.AbsListView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.AmbiguousViewMatcherException
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.FailureHandler
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.adevinta.android.barista.internal.failurehandler.SpyFailureHandler
import com.adevinta.android.barista.internal.failurehandler.description
import com.adevinta.android.barista.internal.failurehandler.withFailureHandler
import com.adevinta.android.barista.internal.viewaction.PerformClickAction.clickUsingPerformClick
import com.adevinta.android.barista.internal.viewaction.ChildActions.onChildWithId
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.Matcher

object BaristaListInteractions {

  @JvmStatic
  @JvmOverloads
  fun clickListItem(@IdRes id: Int? = null, position: Int) {
    performMagicAction(id, position,
        recyclerAction = actionOnItemAtPosition<ViewHolder>(position, clickUsingPerformClick()),
        listViewAction = clickUsingPerformClick()
    )
  }

  @JvmStatic
  @JvmOverloads
  fun scrollListToPosition(@IdRes id: Int? = null, position: Int) {
    performMagicAction(id, position,
        recyclerAction = scrollToPosition<ViewHolder>(position),
        listViewAction = scrollTo()
    )
  }

  @JvmStatic
  @JvmOverloads
  fun clickListItemChild(@IdRes id: Int? = null, position: Int, @IdRes childId: Int) {
    doOnListItemChild(id, position, childId, click())
  }

  @JvmStatic
  @JvmOverloads
  fun doOnListItemChild(@IdRes id: Int? = null, position: Int, @IdRes childId: Int, viewAction: ViewAction) {
    performMagicAction(id, position,
        recyclerAction = actionOnItemAtPosition<ViewHolder>(position, onChildWithId(childId, viewAction)),
        listViewAction = onChildWithId(childId, viewAction)
    )
  }

  private fun performMagicAction(@IdRes id: Int?, position: Int, recyclerAction: ViewAction, listViewAction: ViewAction) {
    verifyOneSingleMatch(id)

    val recyclerMatcher = findRecyclerMatcher(id)
    val listViewMatcher = findListViewMatcher(id)
    val spyFailureHandler = SpyFailureHandler()
    try {
      performOnRecycler(recyclerMatcher, recyclerAction, spyFailureHandler)
    } catch (noRecyclerMatching: NoMatchingViewException) {
      try {
        performOnListView(listViewMatcher, position, listViewAction, spyFailureHandler)
      } catch (listViewError: Throwable) {
        spyFailureHandler
            .resendLastError("Could not perform action (${listViewAction.description}) on ListView ${listViewMatcher.description()}")
      }
    } catch (recyclerError: Throwable) {
      spyFailureHandler
          .resendLastError("Could not perform action (${recyclerAction.description}) on RecyclerView ${recyclerMatcher.description()}")
    }
  }

  private fun verifyOneSingleMatch(@IdRes id: Int?) {
    val spyFailureHandler = SpyFailureHandler()
    try {
      onView(anyOf(findListViewMatcher(id), findRecyclerMatcher(id)))
          .withFailureHandler(spyFailureHandler)
          .check(matches(isDisplayed()))
    } catch (multipleViews: AmbiguousViewMatcherException) {
      spyFailureHandler.resendLastError(
          "There are multiple ListView or RecyclerView in the hierarchy. You must specify an id parameter using clickListItem(id, position)")
    } catch (noneFound: NoMatchingViewException) {
      val message = if (id == null) {
        "No ListView or RecyclerView found in the hierarchy"
      } else {
        "No ListView or RecyclerView with id ${resourceName(id)} was found in the hierarchy. Did you use a wrong id?"
      }
      spyFailureHandler.resendLastError(message)
    }
  }

  private fun performOnRecycler(matcher: Matcher<View>, recyclerAction: ViewAction, failureHandler: FailureHandler) {
    onView(matcher)
        .withFailureHandler(failureHandler)
        .perform(recyclerAction)
  }

  private fun performOnListView(matcher: Matcher<View>, position: Int, listViewAction: ViewAction, failureHandler: FailureHandler) {
    withFailureHandler(failureHandler) {
      onData(anything())
          .inAdapterView(matcher)
          .atPosition(position)
          .perform(listViewAction)
    }
  }

  fun findRecyclerMatcher(@IdRes id: Int?): Matcher<View> = when (id) {
    null -> allOf(isDisplayed(), isAssignableFrom(RecyclerView::class.java))
    else -> allOf(isDisplayed(), isAssignableFrom(RecyclerView::class.java), withId(id))
  }

  fun findListViewMatcher(@IdRes id: Int?): Matcher<View> = when (id) {
    null -> allOf(isDisplayed(), isAssignableFrom(AbsListView::class.java))
    else -> allOf(isDisplayed(), isAssignableFrom(AbsListView::class.java), withId(id))
  }

  private fun resourceName(resId: Int) = ApplicationProvider.getApplicationContext<Context>().resources.getResourceName(resId)
}
