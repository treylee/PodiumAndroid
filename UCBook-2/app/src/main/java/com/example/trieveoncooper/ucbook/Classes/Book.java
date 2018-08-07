package com.example.trieveoncooper.ucbook.Classes;

/**
 * Created by trieveoncooper on 12/4/17.
 */

public class Book {
    private String name;
    private int numOfSongs;
    private int thumbnail;
    private String price;
    private String url;
    String uId;

    public Book() {
    }

    public Book(String name, int numOfSongs, int thumbnail) {
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.thumbnail = thumbnail;
    }public Book(String name,String price,String ur,String ID){
        this.name = name;
        this.price = price;
        this.url = ur;
        this.uId =ID;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String s) {
        this.price = s;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String s) {
        this.url = s;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String s) {
        this.uId = s;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}