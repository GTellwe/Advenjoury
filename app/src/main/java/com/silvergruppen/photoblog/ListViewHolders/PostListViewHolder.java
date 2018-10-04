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

import de.hdodenhof.circleimageview.CircleImageView;

public class PostListViewHolder {
    private LinearLayout textViewWrap;
    private TextView usernameText;
    private TextView dateText;
    private CircleImageView userImage;
    private ImageView image;
    private TextView descTextView;

    public PostListViewHolder(LinearLayout textViewWrap, TextView usernameText,
                                     TextView dateText,CircleImageView userImage, ImageView image, TextView descTextView) {
        super();
        this.textViewWrap = textViewWrap;
        this.usernameText = usernameText;
        this.dateText = dateText;
        this.userImage = userImage;
        this.image = image;
        this.descTextView = descTextView;

    }

    public TextView getDescTextView() {
        return descTextView;
    }

    public void setDescTextView(TextView descTextView) {
        this.descTextView = descTextView;
    }

    public LinearLayout getTextViewWrap() {
        return textViewWrap;
    }

    public void setTextViewWrap(LinearLayout textViewWrap) {
        this.textViewWrap = textViewWrap;
    }

    public TextView getUsernameText() {
        return usernameText;
    }

    public void setUsernameText(TextView usernameText) {
        this.usernameText = usernameText;
    }

    public TextView getDateText() {

        return dateText;

    }

    public void setDateText(TextView dateText) {
        this.dateText = dateText;
    }

    public CircleImageView getUserImage() {
        return userImage;
    }

    public void setUserImage(CircleImageView userImage) {
        this.userImage = userImage;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}