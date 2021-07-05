package com.adevinta.android.barista.internal.viewaction;

import android.view.View;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import org.hamcrest.Matcher;

import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

public class SleepViewAction {

  public static ViewAction sleep(final long millis) {
    return new ViewAction() {
      @Override
      public Matcher<View> getConstraints() {
        return isRoot();
      }

      @Override
      public String getDescription() {
        return "Wait for at least " + millis + " millis";
      }

      @Override
      public void perform(final UiController uiController, final View view) {
        uiController.loopMainThreadUntilIdle();
        uiController.loopMainThreadForAtLeast(millis);
      }
    };
  }
}
