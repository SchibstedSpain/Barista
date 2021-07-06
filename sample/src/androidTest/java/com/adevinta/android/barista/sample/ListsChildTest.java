package com.adevinta.android.barista.sample;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.PerformException;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import com.adevinta.android.barista.internal.failurehandler.BaristaException;
import com.adevinta.android.barista.sample.util.SpyFailureHandlerRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.adevinta.android.barista.interaction.BaristaKeyboardInteractions.closeKeyboard;
import static com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItemChild;
import static com.adevinta.android.barista.interaction.BaristaListInteractions.doOnListItemChild;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(AndroidJUnit4.class)
public class ListsChildTest {

  @Rule
  public ActivityTestRule<ListsActivity> activity = new ActivityTestRule<>(ListsActivity.class, true, false);

  @Rule
  public SpyFailureHandlerRule spyFailureHandlerRule = new SpyFailureHandlerRule();

  @Test
  public void clickRecyclerItemChild() {
    openActivity(ListsActivity.buildIntent()
        .withRecyclers(R.id.recycler)
    );

    clickListItemChild(20, R.id.yes);

    assertClickResult("yes");
    spyFailureHandlerRule.assertNoEspressoFailures();
  }

  @Test
  public void clickListViewItemChild() {
    openActivity(ListsActivity.buildIntent()
        .withComplexLists(R.id.listview)
    );

    clickListItemChild(20, R.id.yes);

    assertClickResult("yes");
    spyFailureHandlerRule.assertNoEspressoFailures();
  }

  @Test
  public void clickGridViewItemChild() {
    openActivity(ListsActivity.buildIntent()
        .withComplexGrids(R.id.gridview)
    );

    clickListItemChild(20, R.id.yes);

    assertClickResult("yes");
    spyFailureHandlerRule.assertNoEspressoFailures();
  }

  @Test
  public void clickMultipleRecyclerItemChild_byId() {
    openActivity(ListsActivity.buildIntent()
        .withRecyclers(R.id.recycler, R.id.recycler2)
    );

    clickListItemChild(R.id.recycler, 20, R.id.yes);

    assertClickResult("yes");
    spyFailureHandlerRule.assertNoEspressoFailures();
  }

  @Test
  public void clickMultipleListViewItemChild_byId() {
    openActivity(ListsActivity.buildIntent()
        .withComplexLists(R.id.listview, R.id.listview2)
    );

    clickListItemChild(R.id.listview, 20, R.id.yes);

    assertClickResult("yes");
    spyFailureHandlerRule.assertNoEspressoFailures();
  }

  @Test
  public void clickMultipleGridViewItemChild_byId() {
    openActivity(ListsActivity.buildIntent()
        .withComplexGrids(R.id.gridview, R.id.gridview2)
    );

    clickListItemChild(R.id.gridview, 20, R.id.yes);

    assertClickResult("yes");
    spyFailureHandlerRule.assertNoEspressoFailures();
  }

  @Test
  public void actionMultipleRecyclerItemChild_byId() {
    openActivity(ListsActivity.buildIntent()
        .withRecyclers(R.id.recycler, R.id.recycler2)
    );

    String text = "It works";
    doOnListItemChild(R.id.recycler, 20, R.id.edittext, replaceText(text));

    assertPerformActionResult(text);
    spyFailureHandlerRule.assertNoEspressoFailures();
  }

  @Test
  public void actionMultipleListViewItemChild_byId() {
    openActivity(ListsActivity.buildIntent()
        .withComplexLists(R.id.listview, R.id.listview2)
    );

    String text = "It works";
    doOnListItemChild(R.id.listview, 20, R.id.edittext, replaceText(text));

    assertPerformActionResult(text);
    spyFailureHandlerRule.assertNoEspressoFailures();
  }

  @Test
  public void actionMultipleGridViewItemChild_byId() {
    openActivity(ListsActivity.buildIntent()
        .withComplexGrids(R.id.gridview, R.id.gridview2)
    );

    String text = "It works";
    doOnListItemChild(R.id.gridview, 20, R.id.edittext, replaceText(text));

    assertPerformActionResult(text);
    spyFailureHandlerRule.assertNoEspressoFailures();
  }

  @Test
  public void fails_whenRecyclerChildNotExist() {
    openActivity(ListsActivity.buildIntent()
        .withRecyclers(R.id.recycler)
    );

    Throwable thrown = catchThrowable(() -> clickListItemChild(20, R.id.not_exists));

    spyFailureHandlerRule.assertEspressoFailures(1);
    assertThat(thrown).isInstanceOf(BaristaException.class)
        .hasMessageContaining(
            "Could not perform action (actionOnItemAtPosition performing ViewAction: Perform single click on a child view with id: ")
        .hasMessageContaining("on item at position: 20) on RecyclerView")
        .hasCauseInstanceOf(PerformException.class)
        .hasStackTraceContaining("Didn't find any view with id");
  }

  @Test
  public void fails_whenListViewChildNotExist() {
    openActivity(ListsActivity.buildIntent()
        .withComplexLists(R.id.listview)
    );

    Throwable thrown = catchThrowable(() -> clickListItemChild(20, R.id.not_exists));

    spyFailureHandlerRule.assertEspressoFailures(1);
    assertThat(thrown).isInstanceOf(BaristaException.class)
        .hasMessageContaining("Could not perform action (Perform single click on a child view with id: ")
        .hasMessageContaining("on ListView")
        .hasCauseInstanceOf(PerformException.class);
  }

  private void openActivity(ListsActivity.IntentBuilder intentBuilder) {
    activity.launchActivity(intentBuilder.build(ApplicationProvider.getApplicationContext()));
    closeKeyboard();
  }

  private void assertClickResult(String text) {
    onView(withId(R.id.clicked_text_result)).check(matches(withText(text)));
  }

  private void assertPerformActionResult(String text) {
    onView(withId(R.id.typed_text_result)).check(matches(withText(text)));
  }
}
