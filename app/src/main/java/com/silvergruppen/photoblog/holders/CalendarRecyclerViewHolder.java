package com.silvergruppen.photoblog.holders;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import com.silvergruppen.photoblog.items.Achievement;

public class CalendarRecyclerViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public ConstraintLayout parentLayout;

    private CheckBox aSwitch;
    private TextView  nameTextView;
    public CalendarRecyclerViewHolder(ConstraintLayout v) {

        super(v);
        parentLayout = v;
        //LinearLayout lin1 = (LinearLayout) v.getChildAt(0);
        //final LinearLayout constraintLayout = (LinearLayout) lin1.getChildAt(0);
        //final LinearLayout linearLayout2 = (LinearLayout) lin1.getChildAt(1);

        aSwitch = (CheckBox) v.getChildAt(2);
        nameTextView = (TextView) v.getChildAt(1);

    }

    public void bind(Achievement achievment){

        boolean open = achievment.isOpen();
        boolean done = achievment.isDone();

        nameTextView.setText(achievment.getName());

    }

    public TextView getNameTextView() {
        return nameTextView;
    }



    public CheckBox getaSwitch() {
        return aSwitch;
    }

    public void setaSwitch(CheckBox aSwitch) {
        this.aSwitch = aSwitch;
    }


}