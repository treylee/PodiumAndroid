package com.example.trieveoncooper.ucbook.Classes;

/**
 * Created by trieveoncooper on 12/6/17.
 */

public class ImageUploadInfo {

    public String imageName;

    public String imagePrice;

    public String imageURL;
    public String uID;

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String name, String url, String Price,String ID) {

        this.imageName = name;
        this.imageURL= url;
        this.imagePrice = Price;
        this.uID = ID;
    }

    public void setImageName(String s){
        imageName = s;
    }
    public void setImagePrice(String s){
        imagePrice = s;
    }


    public void setImageURL(String s) {
        imageURL = s;
    }
    public void setuID(String s){
        uID = s;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageURL() {
        return imageURL;
    }
    public String getImagePrice(){
        return imagePrice;
    }
    public String getuID(){
        return uID;
    }

}