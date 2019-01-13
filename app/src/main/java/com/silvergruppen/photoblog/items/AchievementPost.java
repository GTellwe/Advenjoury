package com.silvergruppen.photoblog.items;

import android.widget.ImageView;

import java.sql.Timestamp;
import java.util.Date;

public class AchievementPost {

    private String text;
    private String image;
    private Date timestamp;
    private ImageView imageView;

    public AchievementPost(String text, String image, Date timestamp) {
        this.text = text;
        this.image = image;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
