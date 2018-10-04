package com.silvergruppen.photoblog.listItems;

import com.silvergruppen.photoblog.ListViewHolders.PostListViewHolder;

import java.sql.Timestamp;
import java.util.Date;

public class PostItem {

    public String userName, image_url, desc, image_thumb, user_image_url;
    public Date timeStamp;


    public PostItem(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public PostItem(){

    }

    public PostItem(String user_id, String image_url, String desc, String image_thumb, Date timeStamp, String user_image_url) {
        this.userName = user_id;
        this.image_url = image_url;
        this.desc = desc;
        this.image_thumb = image_thumb;
        this.timeStamp = timeStamp;
        this.user_image_url = user_image_url;
    }
    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setUser_id(String user_id) {
        this.userName = user_id;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }


    public String getUser_image_url() {
        return user_image_url;
    }

    public void setUser_image_url(String user_image_url) {
        this.user_image_url = user_image_url;
    }

    public String getUser_id() {
        return userName;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage_thumb() {
        return image_thumb;
    }



}
