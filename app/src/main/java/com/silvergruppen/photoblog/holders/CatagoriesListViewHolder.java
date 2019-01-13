package com.silvergruppen.photoblog.holders;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CatagoriesListViewHolder {
  TextView catagorieNameTextView;

    public CatagoriesListViewHolder(final Context context, TextView catagorieNameTextView) {
        super();
        this.catagorieNameTextView = catagorieNameTextView;

    }

    public TextView getCatagorieNameTextView() {
        return catagorieNameTextView;
    }

    public void setCatagorieNameTextView(TextView catagorieNameTextView) {
        this.catagorieNameTextView = catagorieNameTextView;
    }
}