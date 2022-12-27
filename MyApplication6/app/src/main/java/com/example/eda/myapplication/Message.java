package com.example.eda.myapplication;


/**
 * Created by Aymen on 08/06/2018.
 */

public class Message {

    private String nickname;
    private String message ;
    private double latitude;
    private double longtitude;

    public  Message(){

    }
    public Message(String nickname, String message, double latitude, double longtitude) {
        this.nickname = nickname;
        this.message = message;
        this.latitude= latitude;
        this.longtitude = longtitude;


    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
