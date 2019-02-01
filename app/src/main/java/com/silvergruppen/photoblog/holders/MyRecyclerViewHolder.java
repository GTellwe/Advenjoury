package com.silvergruppen.photoblog.holders;

import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.silvergruppen.photoblog.items.Achievement;

import org.w3c.dom.Text;


public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView parentLayout;
        private ConstraintLayout wrap;
        private Switch aSwitch;
        private TextView descriptionTextView, nameTextView;
        private ImageView upArrowImageView, downArrowImageView;
        private ImageView checkImage;
        public MyRecyclerViewHolder(CardView v) {

            super(v);
            parentLayout = v;
            //LinearLayout lin1 = (LinearLayout) v.getChildAt(0);
            //final LinearLayout constraintLayout = (LinearLayout) lin1.getChildAt(0);
            //final LinearLayout linearLayout2 = (LinearLayout) lin1.getChildAt(1);
            ConstraintLayout cons = (ConstraintLayout) v.getChildAt(0);
            aSwitch = (Switch) cons.getChildAt(4);
            descriptionTextView = (TextView) cons.getChildAt(5);
            downArrowImageView = (ImageView) cons.getChildAt(1);
            upArrowImageView = (ImageView) cons.getChildAt(3);
            nameTextView = (TextView) cons.getChildAt(0);
            checkImage = (ImageView) cons.getChildAt(2);
        }

        public void bind(Achievement achievment){

            boolean open = achievment.isOpen();
            boolean done = achievment.isDone();

            descriptionTextView.setVisibility(open ? View.VISIBLE : View.GONE);
            downArrowImageView.setVisibility(open ? View.GONE : View.VISIBLE);
            upArrowImageView.setVisibility(open ? View.VISIBLE : View.GONE);
            checkImage.setVisibility(done? View.VISIBLE : View.GONE);
            aSwitch.setVisibility(open ? View.VISIBLE : View.GONE);
            //aSwitch.setOnClickListener(null);
            //aSwitch.setChecked(achievment.isDone());


            nameTextView.setText(achievment.getName());
            descriptionTextView.setText(achievment.getDesc());




        }

    public TextView getNameTextView() {
        return nameTextView;
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

