package com.silvergruppen.photoblog.ListViewHolders;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.activities.NewPostActivity;

public class AchievementListViewHolder {
    private LinearLayout textViewWrap;
    private TextView textView;
    private LinearLayout journalItems;
    private FloatingActionButton postBtn;
    private Button markAsDoneBtn;
    private ImageView checkImage;
    private TextView pointsText;

    public AchievementListViewHolder(LinearLayout textViewWrap, TextView textView,
                                     FloatingActionButton postBtn, LinearLayout journalItems,
                                     Button markAsDoneBtn, ImageView checkImage,final Context context, TextView pointsText) {
        super();
        this.textViewWrap = textViewWrap;
        this.textView = textView;
        this.journalItems = journalItems;
        this.postBtn = postBtn;
        this.markAsDoneBtn = markAsDoneBtn;
        this.checkImage = checkImage;
        this.pointsText = pointsText;


        // handle the post new button actions


    }

    public TextView getPointsText() {
        return pointsText;
    }

    public void setPointsText(TextView pointsText) {
        this.pointsText = pointsText;
    }

    public ImageView getCheckImage() {
        return checkImage;
    }

    public void setCheckImage(ImageView checkImage) {
        this.checkImage = checkImage;
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
    public void setMarkAsDoneBtnVisability(int visibility){

        markAsDoneBtn.setVisibility(visibility);

    }
    public void setChecImageVisibility(int visibility){

        checkImage.setVisibility(visibility);
    }



}