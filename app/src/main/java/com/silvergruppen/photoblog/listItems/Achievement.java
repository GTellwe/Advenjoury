package com.silvergruppen.photoblog.listItems;

public class Achievement {
    private String name;
    private String points;

    public Achievement(){

    }

    public Achievement(String name, String points) {
        this.name = name;
        this.points = points;
    }

    public String getTopicName() {
        return name;
    }

    public void setTopicName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String members) {
        this.points = members;
    }


}
