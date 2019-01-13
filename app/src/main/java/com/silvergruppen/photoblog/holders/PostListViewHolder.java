package com.silvergruppen.photoblog.holders;


import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostListViewHolder {
    private LinearLayout textViewWrap;
    private TextView usernameText;
    private TextView dateText;
    private CircleImageView userImage;
    private ImageView image;
    private TextView descTextView, headline;
    private  ConstraintLayout rootLayout;

    public PostListViewHolder(TextView usernameText,
                              TextView dateText, CircleImageView userImage,
                              TextView descTextView, TextView headline,
                              ImageView image) {
        super();

        this.usernameText = usernameText;
        this.dateText = dateText;
        this.userImage = userImage;
        this.descTextView = descTextView;
        this.headline = headline;
        this.image = image;

    }

    public TextView getDescTextView() {
        return descTextView;
    }

    public TextView getHeadline() {
        return headline;
    }
/*
    public ConstraintLayout getRootLayout() {
        return rootLayout;
    }

    public void setRootLayout(ConstraintLayout rootLayout) {
        this.rootLayout = rootLayout;
    }
*/
    public void setHeadline(TextView headline) {
        this.headline = headline;
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