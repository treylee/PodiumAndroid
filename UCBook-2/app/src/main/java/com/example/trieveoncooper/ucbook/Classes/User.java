package com.example.trieveoncooper.ucbook.Classes;

import android.media.Image;

/**
 * Created by trieveoncooper on 11/14/17.
 */

public class User {
    private String email;
    private String password;
    private String name= "";
    private String year ="";
    private String bio="";
    private String photoURL;
    private Image photo;
    private String uId ="";
    private boolean firstLogin = true;

    private User() {

    }

    public User(String bio,String email,String name) {
        this.email = email;
        this.password = password;

    }
    public User(boolean a){

    }
    public String getuId(){
        return uId;
    }

    public void setuId(String s){
        uId =s;
   }
    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String year() {
        return year;
    }

    public void setFirstLogin() {
        firstLogin = true;
    }

    public void setName(String s) {
        name = s;
    }

    public void setBio(String s) {
        bio = s;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public String getBio() {
        return bio;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setImage(Image i) {

        photo = i;

    }
    public void setPhotoURL(String s){
        photoURL = s;
    }
    public void setEmail(String s){
        email = s;
    }
}