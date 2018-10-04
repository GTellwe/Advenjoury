package com.silvergruppen.photoblog.listItems;

public class SearchItem {

    private String image;
    private String name;
    private String user_id;

    public SearchItem(String image, String name, String user_id) {
        this.image = image;
        this.name = name;
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
