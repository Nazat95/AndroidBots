package com.example.fatin.foodbasket;

import android.widget.ImageView;

public class Post {
    private String description;
    private String roomNum;
    private String buildingName;
    private String image;
    private String date;


    public Post(String image, String buildingName, String roomNum, String description, String date) {
        this.image = image;
        this.description = description;
        this.roomNum = roomNum;
        this.buildingName = buildingName;
        this.date = date;
    }

    public Post() {

    }

    public String getDescription() {
        return description;
    }


    public void setDescription(String _description) {
        this.description = _description;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
