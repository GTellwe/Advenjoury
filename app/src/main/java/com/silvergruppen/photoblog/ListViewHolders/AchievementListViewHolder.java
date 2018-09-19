package com.silvergruppen.photoblog.ListViewHolders;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.activities.NewPostActivity;

public class AchievementListViewHolder {
    private LinearLayout textViewWrap;
    private TextView textView;
    private LinearLayout journalItems;
    private FloatingActionButton postBtn;

    public AchievementListViewHolder(LinearLayout textViewWrap, TextView textView, FloatingActionButton postBtn, LinearLayout journalItems, final Context context) {
        super();
        this.textViewWrap = textViewWrap;
        this.textView = textView;
        this.journalItems = journalItems;
        this.postBtn = postBtn;


        // handle the post new button actions

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newPostIntent = new Intent(context, NewPostActivity.class);
                Bundle b = new Bundle();
                MainActivity tmpActivity = (MainActivity) context;
                b.putString("topic",tmpActivity.getTopic());
                b.putString("achievement", tmpActivity.getCurrentAchievement());
                newPostIntent.putExtras(b);
                context.startActivity(newPostIntent);
            }
        });
    }

    public LinearLayout getJournalItems() {
        return journalItems;
    }

    public void setJournalItems(LinearLayout journalItems) {
        this.journalItems = journalItems;
    }

    public TextView getTextView() {
        return textView;
    }


    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public LinearLayout getTextViewWrap() {
        return textViewWrap;
    }

    public void setTextViewWrap(LinearLayout textViewWrap) {
        this.textViewWrap = textViewWrap;
    }

    public void setPostBtnVisability(int visability){

        postBtn.setVisibility(visability);
    }



}