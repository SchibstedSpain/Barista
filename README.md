# Barista
**The one who serves a great Espresso**

[![Travis](https://img.shields.io/travis/rust-lang/rust.svg?label=Travis+CI)](https://travis-ci.org/github/AdevintaSpain/Barista)
[![CI](https://github.com/AdevintaSpain/Barista/actions/workflows/main.yml/badge.svg)](https://github.com/AdevintaSpain/Barista/actions/workflows/main.yml)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](LICENSE.md)

<img src="art/barista-logo.svg" width="30%"/>

Barista makes developing UI test faster, easier and more predictable. Built on top of Espresso, it provides a simple and discoverable API, removing most of the boilerplate and verbosity of common Espresso tasks. You and your Android team will write tests with no effort.

 - [Download](#download)
 - [API Overview](#api-overview)
    - [Interactions](#baristas-interactions-api)
    - [Assertions](#baristas-assertions-api)
    - [Intents](#baristas-intents-api)
    - [Runtime Permissions](#runtime-permissions)
    - [Useful test rules](#useful-test-rules)
      - [Reseting app data](#reseting-app-data)
      - [Dealing with Flaky tests](#dealing-with-flaky-tests)
      - [One rule to rule them all](#one-rule-to-rule-them-all)
  - [Magic that Barista does for you](#magic-that-barista-does-for-you)
  - [Contributing](#contributing)
  - [License](#license)

# Download

## New versions (Maven Central)
> We are on it! Follow https://github.com/AdevintaSpain/Barista/issues/382 for updates.

## Old versions (JCenter)
> Previous versions of Barista were published on JCenter with a different dependency name. [Since JCenter doesn't allow uploading dependencies anymore](https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/), they won't be updated. Consider migrating to the newest version of Barista on Maven Central.

Import Barista as a testing dependency:
```gradle
androidTestImplementation('com.schibsted.spain:barista:3.9.0') {
  exclude group: 'org.jetbrains.kotlin' // Only if you already use Kotlin in your project
}
```

You might need to include the Google Maven repository, required by Espresso 3:
```gradle
repositories {
    google()
    jcenter()
}
```

Barista already includes `espresso-core` and `espresso-contrib`. If you need [any other Espresso package](https://developer.android.com/topic/libraries/testing-support-library/packages.html#atsl-dependencies) you can add them yourself.

# API Overview

## Barista’s Interactions API

#### Click widgets
```java
clickOn(R.id.button);
clickOn(R.string.button_text);
clickOn("Next");
clickBack();
```

#### Long click widgets
```java
longClickOn(R.id.button);
longClickOn(R.string.button_text);
longClickOn("Next");
```

#### Click menu items, with or without overflow
```java
clickMenu(R.id.menu_item);
```

#### Open the overflow menu without clicking any item
```java
openMenu();
```

#### Writing into widgets
```java
writeTo(R.id.edittext, "A great text"); // Ignores the EditText restrictions like maxLength and textFilter. It's blazing fast.
typeTo(R.id.edittext, "A great text"); // Honors the EditText restrictions like maxLength and textFilter. It slows down the test.
writeToAutoComplete(R.id.autocomplete, "Another great text");
clearText(R.id.edittext)
```

#### Operate on ListViews and RecyclerViews indistinctly by position
```java
clickListItem(R.id.list, 4);
clickListItemChild(R.id.list, 3, R.id.row_button);
doOnListItemChild(R.id.list, 5, R.id.edittext, replaceText("Yet another great text"));
scrollListToPosition(R.id.list, 4);

clickSpinnerItem(R.id.spinner, 1);
```

#### Select items on RadioButtons
```java
clickRadioButtonItem(R.id.radiogroup, R.id.radio_item);
clickRadioButtonItem(R.id.radiogroup, "The radio text");
clickRadioButtonPosition(R.id.radiogroup, 42);
```

#### Pick data on pickers
```java
setDateOnPicker(1986, 03, 23);
setTimeOnPicker(17, 2);
```

#### Interact with dialogs
```java
clickDialogPositiveButton();
clickDialogNeutralButton();
clickDialogNegativeButton();
```

#### Scroll on scrolls and pagers
```java
scrollTo(R.id.far_away_widget);
scrollTo(R.string.text);
scrollTo("A widget with this text");
scrollTo(withTagValue(is("tagName"))) // using custom matchers
swipeViewPagerForward();
swipeViewPagerBack();
```

#### Interact with the navigation drawer
```java
openDrawer();
openDrawerWithGravity(Gravity.RIGHT);
closeDrawer();
closeDrawerWithGravity(Gravity.RIGHT);
```

#### Interact with SeekBars
```java
setProgressTo(R.id.seek_bar, 5);
setProgressToMin(R.id.seek_bar);
setProgressToMax(R.id.seek_bar);
```

#### Pull to refresh in SwipeRefreshLayout
```java
refresh(R.id.swipe_refresh);
refresh(); // Id is optional. Barista will find it for you.
```

#### Close or press ime actions on the Keyboard
```java
closeKeyboard()
pressImeActionButton()
```

#### And another tricky feature, but try not to use it
```java
sleep(2000);
sleep(2, SECONDS);
```

## Barista’s Assertions API

#### Is this view displayed?
```java
assertDisplayed("Hello world");
assertDisplayed(R.string.hello_world);
assertDisplayed(R.id.button);
assertDisplayed(R.id.button, "Hello world")
assertDisplayed(R.id.button, R.string.hello_world)

// on ListViews and RecyclerViews by position
assertDisplayedAtPosition(R.id.list, 0, "text");
assertDisplayedAtPosition(R.id.list, 0, R.id.text_field, "text");
assertDisplayedAtPosition(R.id.list, 0, R.string.hello_world);
assertDisplayedAtPosition(R.id.list, 0, R.id.text_field, R.string.hello_world);
assertDrawableDisplayedAtPosition(R.id.recycler, 0, R.id.imageview, R.drawable.ic_barista);

// you can also pass custom matchers
assertDisplayed(withTagValue(is("tagName")))

// ...or not?
assertNotDisplayed("Hello world");
assertNotDisplayed(R.string.hello_world);
assertNotDisplayed(R.id.button);
assertNotDisplayed(R.id.button, "Hello world")
assertNotDisplayed(R.id.button, R.string.hello_world)
// you can also pass custom matchers
assertNotDisplayed(withTagValue(is("tagName")))
```

#### Is this view enabled?
```java
assertEnabled("Hello world");
assertEnabled(R.string.hello_world);
assertEnabled(R.id.button);

// ...or not?
assertDisabled("Hello world");
assertDisabled(R.string.hello_world);
assertDisabled(R.id.button);
```

#### Hope this view doesn't exist!
```java
assertNotExist("Hello world");
assertNotExist(R.string.hello_world);
assertNotExist(R.id.button);
```

#### Is the expected checkbox checked?
```java
assertChecked("Checked checkbox");
assertChecked(R.string.checked_checkbox);
assertChecked(R.id.checkbox);

// ...or not?
assertUnchecked("Unchecked checkbox");
assertUnchecked(R.string.unchecked_checkbox);
assertUnchecked(R.id.checkbox);
```

#### Is this view clickable?
```java
assertClickable("Hello world")
assertClickable(R.string.hello_world)
assertClickable(R.id.button)

// ...or not?
assertNotClickable("Hello world")
assertNotClickable(R.string.hello_world)
assertNotClickable(R.id.button)
```

#### Does this view have the focus?
```java
assertFocused(R.id.focused_view)
assertFocused("Button")

// ...or not?
assertNotFocused(R.id.focused_view)
assertNotFocused("Button")
```

#### Is this ImageView showing a drawable?
```java
assertHasAnyDrawable(R.id.image_view);
assertHasDrawable(R.id.image_view, R.drawable.ic_barista);

// ...or not?
assertHasNoDrawable(R.id.image_view);
```

#### Does this View have a background?
```java
assertHasAnyBackground(R.id.view);
assertHasBackground(R.id.view, R.drawable.ic_barista);

// ...or not?
assertHasNoBackground(R.id.view);
```

#### Does this View have content description?
```java
assertHasContentDescription(R.id.anyView);
assertContentDescription(R.id.anyView, R.string.content_description);
assertContentDescription(R.id.anyView, "Some text");
```

#### Is this List empty? How many items does it have?
````java
// Works with both ListView and RecyclerView
assertListNotEmpty(R.id.list)
assertListItemCount(R.id.list, 5)

// You can also pass custom assertions
assertCustomAssertionAtPosition(R.id.list, 0, customViewAssertion);
````

#### What's the state of the Drawer?
```java
assertDrawerIsOpen();
assertDrawerIsOpenWithGravity(Gravity.RIGHT);
assertDrawerIsClosed();
assertDrawerIsClosedWithGravity(Gravity.RIGHT);
```

#### Check TextInputLayout and EditText's hints
```java
assertHint(R.id.edittext, R.string.hint);
assertHint(R.id.edittext, "Hint");
```

#### Check TextInputLayout and EditText's errors
```java
assertErrorDisplayed(R.id.edittext, R.string.error);
assertErrorDisplayed(R.id.edittext, "Error message");

assertNoErrorDisplayed(R.id.edittext, R.string.error);
assertNoErrorDisplayed(R.id.edittext, "Error message");
```

#### Check TextInputLayout's assistive helper text
```java
assertAssistiveText(R.id.textinputlayout, R.string.helper_text);
assertAssistiveText(R.id.textinputlayout, "Helper text");
```

#### Check if text on screen contains given text
```java
assertContains("text");
assertContains(R.string.text);
assertContains(R.id.textview, "text");
assertContains(R.id.textview, R.string.text);

// ...or not?
assertNotContains("text");
assertNotContains(R.string.text);
assertNotContains(R.id.textview, "text");
assertNotContains(R.id.textview, R.string.text);
```

#### Check text is given color
```java
assertTextColorIs(R.id.some_red_text, R.color.red);
assertTextColorIs(R.id.some_color_list_text, R.color.state_list);

// ...or not?
assertTextColorIsNot(R.id.some_red_text, R.color.blue);
assertTextColorIsNot(R.id.some_color_list_text, R.color.another_state_list);
```

`assertTextColorIs` and its variant `assertTextColorIsNot` work with:

- *Color int*: `Color.parse("#ff00ff")`
- *Color resource*: `R.color.green`
- *Color attribute*: `R.attr.colorPrimary`

Also Barista can check colors parsed from `declarable-style` custom attribute:
```java
assertTextColorIs(R.id.customTextView, R.styleable.SampleCustomView, R.style.SampleCustomStyle, R.styleable.SampleCustomView_customColor);

// ...or not?
assertTextColorIsNot(R.id.customTextView, R.styleable.SampleCustomView, R.style.SampleCustomStyle_Green, R.styleable.SampleCustomView_customColor);
```

#### Check recyclerView item count against expected item count
```java
assertRecyclerViewItemCount(R.id.recycler, 10);
```

#### Is this ProgressBar/SeekBar progress?
```java
assertProgress(R.id.seek_bar, 5)
assertProgressIsMin(R.id.seek_bar)
assertProgressIsMax(R.id.seek_bar)
```

#### And another tricky feature
```java
assertThatBackButtonClosesTheApp();
```

### Custom assertions

If you have a special case not covered by the given assertions API, we encourage you to assert these special cases with our custom assertions API. It's a convenient way to replace plain `Matcher`s with complex assertions. With Barista, you can match any kind of view by knowing its type and passing its `viewId`, `text`, or a `Matcher<View>`. Once you matched it, you will be able to assert all its properties without adding any complex `Matcher` to your project.

```kotlin

// Matching a Button by text
assertAny<Button>("Save") {
    it.enabled == true
}

// Matching a RadioGroup by id
assertAny<RadioGroup>(R.id.radioGroup) {
    it.checkedRadioButtonId == R.id.option1
}

// Matching a Progressbar by a Matcher
assertAny<Progressbar>(withId(R.id.progressBar)) {
    it.progress == 42
}

// You can also define the assertion description that will be shown if the assertion fails
assertAny<RadioGroup>(R.id.radioGroup, "selected option is the second one") {
    it.checkedRadioButtonId == R.id.option1
}
```

## Mocking the Intent results

Mocking the Android Camera Intent is a tricky thing to do. To accomplish it in no time, Barista gives a way to do it in one line: the method `mockAndroidCamera()`. This method does all the magic to mock the result of the camera. One more thing to do: you have to call `Intents.init()` before calling `mockAndroidCamera()`, and `Intents.release()` after doing the action that launches the camera. You could also use `IntentsTestRule` instead of the common `ActivityTestRule` to skip it, but as we recommend the use of `BaristaRule`, it's easier to just call both methods manually when needed.

Here's an example to copy paste:

```java
try {
  Intents.init();
  BaristaIntents.mockAndroidCamera();
  clickOn(R.id.launch_camera);
} finally {
  Intents.release();
}
```

## Runtime Permissions
The new Marshmallow permissions system requires checking for permissions at runtime. As Espresso can't interact with the system dialog, Barista offers a way to allow permissions when needed.

```java
PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.GET_ACCOUNTS);
```
```java
PermissionGranter.allowPermissionOneTime(Manifest.permission.GET_ACCOUNTS);
```

## Useful test rules
Barista includes a set of useful test rules to help you:

### Reseting app data

As tests should be isolated, they need to set the environment before running. Espresso doesn't help achieving it but Barista offers a set of rules to clear the app's data before running each test.

```java
// Clear all app's SharedPreferences
@Rule public ClearPreferencesRule clearPreferencesRule = new ClearPreferencesRule();

// Delete all tables from all the app's SQLite Databases
@Rule public ClearDatabaseRule clearDatabaseRule = new ClearDatabaseRule();

// Delete all files in getFilesDir() and getCacheDir()
@Rule public ClearFilesRule clearFilesRule = new ClearFilesRule();
```


### Dealing with Flaky tests

We should try to write deterministic tests, but when everything else fails Barista helps you deal with flaky tests using a specific ActivityTestRule and a couple of annotations that repeat your tests multiple times.

```java
// Use a RuleChain to wrap your ActivityTestRule with a FlakyTestRule
private ActivityTestRule<FlakyActivity> activityRule = new ActivityTestRule<>(FlakyActivity.class);
private FlakyTestRule flakyRule = new FlakyTestRule();

@Rule
public RuleChain chain = RuleChain.outerRule(flakyRule)
    .around(activityRule);


// Use @AllowFlaky to let flaky tests pass if they pass any time.
@Test
@AllowFlaky(attempts = 5)
public void some_flaky_test() throws Exception {
  // ...
}

// Use @Repeat to avoid flaky tests from passing if any repetition fails.
@Test
@Repeat(times = 5)
public void some_important_test() throws Exception {
  // ...
}
```

### One rule to rule them all

All previous rules can be added at the same time by just adding the BaristaRule.
```java
@Rule
public BaristaRule<MyActivity> baristaRule = BaristaRule.create(MyActivity.class);

//...

baristaRule.launchActivity();
```

The rule assumes some sane defaults:
- Retry flaky tests: 10 attempts
- Launch activity automatically: false
- Initial touch mode enabled: true
- Clear preferences
- Clear databases
- Clear files

### Writing tests in Kotlin?
[Check this link](https://github.com/AdevintaSpain/Barista/issues/219) to know how to use `@Rule` in Kotlin.

# Magic that Barista does for you

In order to speed up testing, Barista keeps in mind some considerations.
- **Scrolls when needed**: Interacting with Espresso in a `ScrollView` requires you to scroll to each view, which sometimes doesn't work the first time. Also trying to scroll outside a `ScrollView` produces an `Exception`, forcing you to change the test depending on the layout. To keep tests simpler, Barista scrolls automatically before interacting with any `View`, and only does it if needed.
- **Scrolls on all views**: Barista scrolls on all scrollable views, including `NestedScrollView`. Espresso only handles `ScrollView` and `HorizontalScrollView`, so people need to open questions on [StackOverflow like this](https://stackoverflow.com/questions/35272953/espresso-scrolling-not-working-when-nestedscrollview-or-recyclerview-is-in-coor). Or... just use **Barista**.
- **Just interacts with displayed Views**: Interacting with `View`s inside a `ViewPager` throws `AmbiguousViewMatcherException`, because the views you interact with will be potentially repeated on different pages. Barista only interacts with displayed widgets, so you can focus on the behavior instead of wasting time on details.

# Contributing

We welcome contributions! If you found a bug or have a feature request, feel free to [open an issue](https://github.com/AdevintaSpain/Barista/issues/new) to discuss it. Remember that bugs reported with a reproducible test are more likely to be investigated and fixed. You can also submit a Pull Request.

## Formatting
We use our company's IntelliJ code style for the project, which is very similar to the official Kotlin Android code style. When submitting code please make sure you use the proper format. You can install the code style into Android Studio by running the script in `./config/androidstudio/install-codestyle.sh`. Then restart Android Studio and pick the "BaristaAndroid" schema in preferences.

## Prefer Java-written test classes
As most parts of Barista are Java-compatible, please do write Java tests when possible. Writing them in Kotlin might lead to using Kotlin-only shortcuts, breaking the Java compatibility Barista aims for.

# License
**[Apache License, Version 2.0 (the "License")](LICENSE.md)**
