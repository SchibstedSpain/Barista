package com.adevinta.android.barista.sample;

import android.app.Activity;
import android.content.Context;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class TextListViewAdapter extends BaseAdapter {
  private Activity activity;
  private String[] items;
  private final TextView clickedResult;
  private final TextWatcher textWatcher;

  TextListViewAdapter(Activity activity, String[] items, TextView clickedResult, TextWatcher textWatcher) {
    this.activity = activity;
    this.items = items.clone();
    this.clickedResult = clickedResult;
    this.textWatcher = textWatcher;
  }

  @Override
  public int getCount() {
    return items.length;
  }

  @Override
  public Object getItem(int position) {
    return items[position];
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    View rowView = convertView;

    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      rowView = inflater.inflate(R.layout.row_with_image_and_buttons, parent, false);
    }

    TextView textView = (TextView) rowView.findViewById(R.id.textview);
    View yesButton = rowView.findViewById(R.id.yes);
    View noButton = rowView.findViewById(R.id.no);
    EditText editText = rowView.findViewById(R.id.edittext);

    textView.setText(items[position]);

    ImageView imageView = rowView.findViewById(R.id.imageview);
    if (position == 0 || position == 1) {
      imageView.setImageResource(R.drawable.ic_barista);
    } else {
      imageView.setImageResource(R.drawable.ic_action_menu);
    }

    rowView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        clickedResult.setText(ListsActivity.getComplexListViewTextAt(position));
      }
    });
    yesButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        clickedResult.setText("yes");
      }
    });
    noButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        clickedResult.setText("no");
      }
    });

    // Avoiding adding TextWatcher multiple times
    editText.removeTextChangedListener(textWatcher);
    editText.addTextChangedListener(textWatcher);

    return rowView;
  }
}
