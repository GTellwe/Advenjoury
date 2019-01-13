package com.silvergruppen.photoblog.holders;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AchievementListViewHolder {
    private LinearLayout textViewWrap;
    private TextView textView;
    private LinearLayout journalItems;
    private FloatingActionButton postBtn;
    private Button markAsDoneBtn;
    private ImageView checkImage;
    private TextView pointsText;

    public AchievementListViewHolder(final Context context) {
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