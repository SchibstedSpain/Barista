package com.adevinta.android.barista.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

/**
 * This Activity helps testing a variety of scenarios with different ListView and RecyclerView.
 * We can dynamically add ListViews or RecyclerViews with intent parameters.
 */
public class ListsActivity extends AppCompatActivity {

  private static final String EXTRA_SIMPLE_LISTS = "simpleLists";
  private static final String EXTRA_COMPLEX_LISTS = "complexLists";
  private static final String EXTRA_RECYCLERS = "recyclers";
  private static final String EXTRA_SIMPLE_GRIDS = "simpleGrids";
  private static final String EXTRA_COMPLEX_GRIDS = "complexGrids";

  static final String[] FRUITS = new String[] {
      "Apple", "Apricot", "Avocado", "Banana", "Bilberry", "Blackberry", "Blackcurrant",
      "Blueberry", "Boysenberry", "Currant", "Cherry", "Cherimoya", "Cloudberry", "Coconut",
      "Cranberry", "Cucumber", "Custardapple", "Damson", "Date", "Dragonfruit", "Durian",
      "Elderberry", "Feijoa", "Fig", "Gojiberry", "Gooseberry", "Grape", "Raisin",
      "Grapefruit", "Guava", "Honeyberry", "Huckleberry", "Jabuticaba", "Jackfruit", "Jambul",
      "Jujube", "Juniperberry", "Kiwifruit", "Kumquat", "Lemon", "Lime", "Loquat",
      "Longan", "Lychee", "Mango", "Marionberry", "Melon", "Cantaloupe", "Honeydew",
      "Watermelon", "Miraclefruit", "Mulberry", "Nectarine", "Nance", "Olive", "Orange",
      "Bloodorange", "Clementine", "Mandarine", "Tangerine", "Papaya", "Passionfruit", "Peach",
      "Pear", "Persimmon", "Physalis", "Plantain", "Plum", "Prune(driedplum)", "Pineapple",
      "Plumcot(orPluot)", "Pomegranate", "Pomelo", "Purplemangosteen", "Quince", "Raspberry",
      "Salmonberry", "Rambutan", "Redcurrant", "Salalberry", "Salak", "Satsuma", "Starfruit",
      "Solanumquitoense", "Strawberry", "Tamarillo", "Tamarind", "Uglifruit", "Yuzu"
  };

  public static String getSimpleListViewTextAt(int position) {
    return FRUITS[position] + " simple listview clicked";
  }

  public static String getComplexListViewTextAt(int position) {
    return FRUITS[position] + " complex listview clicked";
  }

  public static String getRecyclerViewTextAt(int position) {
    return FRUITS[position] + " recyclerview clicked";
  }

  public static IntentBuilder buildIntent() {
    return new IntentBuilder();
  }

  private LinearLayout listsContainer;
  private TextView clickedResult;
  private TextView typedResult;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lists);
    listsContainer = (LinearLayout) findViewById(R.id.multi_list_container);
    clickedResult = (TextView) findViewById(R.id.clicked_text_result);
    typedResult = (TextView) findViewById(R.id.typed_text_result);

    for (int id : getIntent().getIntArrayExtra(EXTRA_SIMPLE_LISTS)) {
      addSimpleListView(id);
    }
    for (int id : getIntent().getIntArrayExtra(EXTRA_COMPLEX_LISTS)) {
      addComplexListView(id);
    }
    for (int id : getIntent().getIntArrayExtra(EXTRA_RECYCLERS)) {
      addRecyclerView(id);
    }

    for (int id : getIntent().getIntArrayExtra(EXTRA_SIMPLE_GRIDS)) {
      addSimpleGridView(id);
    }

    for (int id : getIntent().getIntArrayExtra(EXTRA_COMPLEX_GRIDS)) {
      addComplexGridView(id);
    }
  }

  private void addSimpleListView(int id) {
    ListView listView = new ListView(this);
    listView.setId(id);
    listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, FRUITS));
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        clickedResult.setText(getSimpleListViewTextAt(position));
      }
    });
    addList(listView);
  }

  private void addComplexListView(int id) {
    ListView listView = new ListView(this);
    listView.setId(id);
    listView.setAdapter(new TextListViewAdapter(this, FRUITS, clickedResult, createTypedResultTextWatcher()));
    addList(listView);
  }

  private void addRecyclerView(int id) {
    RecyclerView recyclerView = new RecyclerView(this);
    recyclerView.setId(id);
    recyclerView.setHasFixedSize(true);

    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setAdapter(new TextRecyclerViewAdapter(FRUITS, clickedResult, createTypedResultTextWatcher()));
    addList(recyclerView);
  }

  private void addSimpleGridView(int id) {
    GridView gridView = new GridView(this);
    gridView.setId(id);
    gridView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, FRUITS));
    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        clickedResult.setText(getSimpleListViewTextAt(position));
      }
    });
    addList(gridView);
  }

  private void addComplexGridView(int id) {
    GridView gridView = new GridView(this);
    gridView.setId(id);
    gridView.setAdapter(new TextListViewAdapter(this, FRUITS, clickedResult, createTypedResultTextWatcher()));
    addList(gridView);
  }

  private void addList(View listView) {
    listsContainer.addView(listView,
        new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            1
        ));
  }

  @NotNull
  private TextWatcher createTypedResultTextWatcher() {
    return new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        typedResult.setText(s);
      }
    };
  }

  public static class IntentBuilder {
    private int[] simpleListViewIds = {};
    private int[] complexListViewIds = {};
    private int[] recyclerViewIds = {};
    private int[] simpleGridViewIds = {};
    private int[] complexGridViewIds = {};

    public IntentBuilder withSimpleLists(int... ids) {
      simpleListViewIds = ids;
      return this;
    }

    public IntentBuilder withComplexLists(int... ids) {
      complexListViewIds = ids;
      return this;
    }

    public IntentBuilder withRecyclers(int... ids) {
      recyclerViewIds = ids;
      return this;
    }

    public IntentBuilder withSimpleGrids(int... ids) {
      simpleGridViewIds = ids;
      return this;
    }

    public IntentBuilder withComplexGrids(int... ids) {
      complexGridViewIds = ids;
      return this;
    }

    public Intent build(Context context) {
      Intent intent = new Intent(context, ListsActivity.class);
      intent.putExtra(EXTRA_SIMPLE_LISTS, simpleListViewIds);
      intent.putExtra(EXTRA_COMPLEX_LISTS, complexListViewIds);
      intent.putExtra(EXTRA_RECYCLERS, recyclerViewIds);
      intent.putExtra(EXTRA_SIMPLE_GRIDS, simpleGridViewIds);
      intent.putExtra(EXTRA_COMPLEX_GRIDS, complexGridViewIds);
      return intent;
    }
  }
}
