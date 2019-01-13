package com.silvergruppen.photoblog.holders;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;


public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView parentLayout;
        private ConstraintLayout wrap;
        private Switch aSwitch;
        private TextView descriptionTextView;
        private ImageView upArrowImageView, downArrowImageView;
        public MyRecyclerViewHolder(CardView v) {
            super(v);
            parentLayout = v;
        }

    public ConstraintLayout getWrap() {
        return wrap;
    }

    public void setWrap(ConstraintLayout wrap) {
        this.wrap = wrap;
    }

    public Switch getaSwitch() {
        return aSwitch;
    }

    public void setaSwitch(Switch aSwitch) {
        this.aSwitch = aSwitch;
    }

    public TextView getDescriptionTextView() {
        return descriptionTextView;
    }

    public void setDescriptionTextView(TextView descriptionTextView) {
        this.descriptionTextView = descriptionTextView;
    }

    public ImageView getUpArrowImageView() {
        return upArrowImageView;
    }

    public void setUpArrowImageView(ImageView upArrowImageView) {
        this.upArrowImageView = upArrowImageView;
    }

    public ImageView getDownArrowImageView() {
        return downArrowImageView;
    }

    public void setDownArrowImageView(ImageView downArrowImageView) {
        this.downArrowImageView = downArrowImageView;
    }
}

