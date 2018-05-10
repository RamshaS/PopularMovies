package com.ramshasaeed.popularmovies.utilities;

import com.ramshasaeed.popularmovies.model.Movie;

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
}
