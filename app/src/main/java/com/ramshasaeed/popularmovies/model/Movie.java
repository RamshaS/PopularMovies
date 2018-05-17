package com.ramshasaeed.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RamSl-la Saeed on 10-May-18.
 */

public class Movie implements Parcelable {
    public Movie() {
    }

    public Movie(int id, String posterUrl, String original_title, String overview, double vote_average, String release_date) {
        this.id = id;
        this.posterUrl = posterUrl;
        this.original_title = original_title;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }

    private int id;
    private String posterUrl;
    private String original_title;
    private String overview;
    private double vote_average;
    private String release_date;

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(posterUrl);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeDouble(vote_average);
    }
    private Movie(Parcel in) {
        id = in.readInt();
        posterUrl = in.readString();
        original_title = in.readString();
        overview = in.readString();
        release_date = in.readString();
        vote_average = in.readDouble();
    }


}
