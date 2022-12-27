package com.example.eda.myapplication;

import android.location.LocationManager;

import static java.util.Collections.list;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*
User class is a class that is created entirely for user information.The wall information that is allowed
with all the wall information, including id, name, username, username password, birthdate,
location, telephone, email, address, is included in this class.
*/
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String surname;
    private String username;
    private String password;
    //private Location location;
    private String email;
    private String phone;
    private String address;
    private List<Wall> allowedWall;
    private List allWalls;

    public User(){

    }
    public User(int id, String name, String surname, String username, String password, String email, String phone, String address){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public void updateLocation(){

    }

    public void updateAllowed(){
        /*Update of the allowed wall is done with this method.*/
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getSurname(){
        return surname;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    /*public Location getLocation(){
        return location;
    }*/


    public String getEmail(){
        return email;
    }

    public String getAddress(){
        return address;
    }

    public List<Wall> getAllowedWall(){
        return allowedWall;
    }

    public List getAllWalls(){
        return allWalls;
    }

    public void setId(int theId){
        id = theId;
    }

    public void setName(String theName){
        name = theName;
    }

    public void setSurname(String theSurname){
        surname = theSurname;
    }

    public void setUsername(String theUsername){
        username = theUsername;
    }

    public void setPassword(String thePassword){
        password = thePassword;
    }

    /*public void setLocation(Location theLocation){
        location = theLocation;
    }*/

    public void setEmail(String theEmail){
        email = theEmail;
    }

    public void setAddress(String theAddress){
        address = theAddress;
    }

    public void setAllowedWall(List<Wall> theAllowed){
        allowedWall = theAllowed;
    }

    public void setAllWalls(List theAllWalls){
        allWalls = theAllWalls;
    }

}
