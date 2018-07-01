package com.ramshasaeed.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Reviews implements Parcelable{
private String id;
private String author;
private String content;
private String url;

    public Reviews() {
    }
    private Reviews(Parcel in){
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public static final Creator<Reviews> CREATOR = new Creator<Reviews>() {
    @Override
    public Reviews createFromParcel(Parcel parcel) {
        return new Reviews(parcel);
    }

    @Override
    public Reviews[] newArray(int i) {
        return new Reviews[i];
    }
};
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(content);
        parcel.writeString(url);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
