package com.w4ma.soft.tamenly.CategoryActivities.Models.HouseModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class HouseList extends RealmObject {
    public HouseList(int realmID) {
        this.realmID = realmID;
    }

    @PrimaryKey
    private int realmID;
    private String post_id;
    private String post_title;
    private String post_real_price;
    private String currency;
    private String post_description;
    private String post_image;
    private String post_datetime;
    private String category;
    private String user_name;
    private String user_pp;
    private  String user_ID;

    public HouseList(String post_id, String post_title, String post_real_price, String currency, String post_description, String post_image, String post_datetime, String category, String user_name, String user_pp,String user_ID) {
        this.post_id = post_id;
        this.post_title = post_title;
        this.post_real_price = post_real_price;
        this.currency = currency;
        this.post_description = post_description;
        this.post_image = post_image;
        this.post_datetime = post_datetime;
        this.category = category;
        this.user_name = user_name;
        this.user_pp = user_pp;
        this.user_ID =user_ID;
    }

    public HouseList() {
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_real_price() {
        return post_real_price;
    }

    public void setPost_real_price(String post_real_price) {
        this.post_real_price = post_real_price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPost_description() {
        return post_description;
    }

    public void setPost_description(String post_description) {
        this.post_description = post_description;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getPost_datetime() {
        return post_datetime;
    }

    public void setPost_datetime(String post_datetime) {
        this.post_datetime = post_datetime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pp() {
        return user_pp;
    }

    public void setUser_pp(String user_pp) {
        this.user_pp = user_pp;
    }
    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }
    public int getRealmID() {
        return realmID;
    }

    public void setRealmID(int realmID) {
        this.realmID = realmID;
    }



}