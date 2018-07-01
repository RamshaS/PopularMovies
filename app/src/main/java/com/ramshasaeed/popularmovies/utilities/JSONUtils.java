package com.ramshasaeed.popularmovies.utilities;

import com.ramshasaeed.popularmovies.model.Movie;
import com.ramshasaeed.popularmovies.model.Reviews;
import com.ramshasaeed.popularmovies.model.Trailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by RamSl-la Saeed on 10-May-18.
 */

public class JSONUtils {
    public static ArrayList<Movie> parseMovieJson(String json) {
        ArrayList<Movie> movielist = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonResult = jsonObject.getJSONArray("results");
            Movie movie;
            movielist = new ArrayList<>();
            for (int i = 0; i < jsonResult.length(); i++) {
                movie = new Movie();
                JSONObject movieItem = jsonResult.getJSONObject(i);
                movie.setId(movieItem.getInt("id"));
                movie.setPosterUrl(movieItem.getString("poster_path"));
                movie.setOriginal_title(movieItem.getString("original_title"));
                movie.setOverview(movieItem.getString("overview"));
                movie.setVote_average(movieItem.getDouble("vote_average"));
                movie.setRelease_date(movieItem.getString("release_date"));
                movielist.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movielist;
    }
    public static ArrayList<Reviews> parseReviewsJson(String json) {
        ArrayList<Reviews> reviewslist = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonResult = jsonObject.getJSONArray("results");
            Reviews reviews;
            reviewslist = new ArrayList<>();
            for (int i = 0; i < jsonResult.length(); i++) {
                reviews = new Reviews();
                JSONObject review = jsonResult.getJSONObject(i);
                reviews.setId(review.getString("id"));
                reviews.setAuthor(review.getString("author"));
                reviews.setContent(review.getString("content"));
                reviews.setUrl(review.getString("url"));
                reviewslist.add(reviews);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewslist;
    }
    public static ArrayList<Trailers> parseTrailerJson(String json) {
        ArrayList<Trailers> trailerList = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonResult = jsonObject.getJSONArray("results");
            Trailers trailers;
            trailerList = new ArrayList<>();
            for (int i = 0; i < jsonResult.length(); i++) {
                trailers = new Trailers();
                JSONObject trailer = jsonResult.getJSONObject(i);
                trailers.setId(trailer.getString("id"));
                trailers.setIso_639_1(trailer.getString("iso_639_1"));
                trailers.setIso_3166_1(trailer.getString("iso_3166_1"));
                trailers.setKey(trailer.getString("key"));
                trailers.setName(trailer.getString("name"));
                trailers.setSite(trailer.getString("site"));
                trailers.setSize(trailer.getInt("size"));
                trailers.setType(trailer.getString("type"));
                if (trailers.getType().equals("Trailer")){
                    trailerList.add(trailers);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailerList;
    }
}
