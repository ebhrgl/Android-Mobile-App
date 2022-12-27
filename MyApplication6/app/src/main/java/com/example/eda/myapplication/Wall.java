package com.example.eda.myapplication;

public class Wall {
    private String wallname;
    private double latitude;
    private double longtitude;
    private int id;

    public Wall(){

    }

    public Wall(int id, String wallname, double latitude, double longtitude){
        this.wallname = wallname;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.id = id;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWallname() {
        return wallname;
    }

    public void setWallname(String wallname) {
        this.wallname = wallname;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

}
