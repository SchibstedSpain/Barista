package com.adevinta.android.barista.sample;

import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import static com.adevinta.android.barista.sample.ListsActivity.getRecyclerViewTextAt;

public class TextRecyclerViewAdapter extends RecyclerView.Adapter<TextRecyclerViewAdapter.ViewHolder> {
  private final String[] items;
  private final TextView clickedResult;
  private final TextWatcher textWatcher;

  TextRecyclerViewAdapter(String[] items, TextView clickedResult, TextWatcher textWatcher) {
    this.items = items.clone();
    this.clickedResult = clickedResult;
    this.textWatcher = textWatcher;
  }

  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_with_image_and_buttons, parent, false);
    TextView textView = (TextView) root.findViewById(R.id.textview);
    View yesButton = root.findViewById(R.id.yes);
    View noButton = root.findViewById(R.id.no);
    EditText editText = root.findViewById(R.id.edittext);
    return new ViewHolder(root, textView, yesButton, noButton, editText, textWatcher);
  }

  public void onBindViewHolder(final ViewHolder holder, final int position) {
    holder.textView.setText(items[position]);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        clickedResult.setText(getRecyclerViewTextAt(holder.getAdapterPosition()));
      }
    });
    holder.yesButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        clickedResult.setText("yes");
      }
    });
    holder.noButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        clickedResult.setText("no");
      }
    });
  }

  public int getItemCount() {
    return items.length;
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    View yesButton;
    View noButton;
    EditText editText;

    ViewHolder(View root, TextView textView, View yesButton, View noButton, EditText editText, TextWatcher textWatcher) {
      super(root);
      this.textView = textView;
      this.yesButton = yesButton;
      this.noButton = noButton;
      this.editText = editText;

      // Avoiding adding TextWatcher multiple times
      this.editText.removeTextChangedListener(textWatcher);
      this.editText.addTextChangedListener(textWatcher);
    }
  }
}
