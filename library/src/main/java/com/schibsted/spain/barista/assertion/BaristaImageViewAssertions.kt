@file:JvmName("BaristaImageViewAssertions")

package com.schibsted.spain.barista.assertion

import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.schibsted.spain.barista.internal.assertAny
import com.schibsted.spain.barista.internal.matcher.DrawableMatcher.Companion.withAnyDrawable
import com.schibsted.spain.barista.internal.matcher.DrawableMatcher.Companion.withDrawable
import com.schibsted.spain.barista.internal.matcher.DrawableMatcher.Companion.withoutDrawable

fun assertHasDrawable(@IdRes imageViewId: Int, @DrawableRes drawable: Int) {
  withId(imageViewId).assertAny(withDrawable(drawable))
}

fun assertHasAnyDrawable(@IdRes imageViewId: Int) {
  withId(imageViewId).assertAny(withAnyDrawable())
}

fun assertHasNoDrawable(@IdRes imageViewId: Int) {
  withId(imageViewId).assertAny(withoutDrawable())
}
