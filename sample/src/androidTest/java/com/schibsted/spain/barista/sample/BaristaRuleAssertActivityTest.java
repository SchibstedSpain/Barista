package com.schibsted.spain.barista.sample;

import android.support.test.runner.AndroidJUnit4;
import com.schibsted.spain.barista.rule.BaristaRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class BaristaRuleAssertActivityTest {

  @Rule
  public BaristaRule<HelloWorldActivity> baristaRule = BaristaRule.create(HelloWorldActivity.class);

  @Test
  public void check_activity_is_finished() {
    baristaRule.launchActivity();

    clickOn(R.id.btn_closed);

    assertTrue(baristaRule.getActivityTestRule().getActivity().isFinishing());
  }
}
