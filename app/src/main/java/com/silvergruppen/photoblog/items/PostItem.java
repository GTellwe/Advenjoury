package com.silvergruppen.photoblog.items;

import java.util.Date;

public class PostItem {

    public String userName, image_url, desc, image_thumb, user_image_url, user_id, achievementName, topic, headline;
    public Date timeStamp;


    public PostItem(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public PostItem(){

    }

    public PostItem(String userName, String image_url, String desc, String image_thumb,
                    Date timeStamp, String user_image_url, String user_id, String topic,
                    String achievementName, String headline) {
        this.userName = userName;
        this.image_url = image_url;
        this.desc = desc;
        this.image_thumb = image_thumb;
        this.timeStamp = timeStamp;
        this.user_image_url = user_image_url;
        this.user_id = user_id;
        this.topic = topic;
        this.achievementName = achievementName;
        this.headline = headline;

    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getAchievementName() {
        return achievementName;
    }

    public void setAchievementName(String achievementName) {
        this.achievementName = achievementName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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
        return user_id;
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


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
