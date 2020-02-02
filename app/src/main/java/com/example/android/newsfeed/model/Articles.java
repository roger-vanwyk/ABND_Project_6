package com.example.android.newsfeed.model;

import android.os.Parcel;
import android.os.Parcelable;

// Created on 24/01/2020 by Roger van Wyk.

public class Articles implements Parcelable {

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType(String type) {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate(String date) {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String category;
    String title;
    String type;
    String date;
    String url;

    public Articles(String category, String title, String type, String date, String url) {
        this.category = category;
        this.title = title;
        this.type = type;
        this.date = date;
        this.url = url;
    }

    protected Articles(Parcel in) {
        category = in.readString ( );
        title = in.readString ( );
        type = in.readString ( );
        date = in.readString ( );
        url = in.readString ( );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString (category);
        dest.writeString (title);
        dest.writeString (type);
        dest.writeString (date);
        dest.writeString (url);
    }

    public static final Parcelable.Creator<Articles> CREATOR = new Parcelable.Creator<Articles> ( ) {
        @Override
        public Articles createFromParcel(Parcel in) {
            return new Articles (in);
        }

        @Override
        public Articles[] newArray(int size) {
            return new Articles[size];
        }
    };
}

