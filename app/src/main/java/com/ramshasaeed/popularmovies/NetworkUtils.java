package com.ramshasaeed.popularmovies;

import android.net.Uri;

import com.ramshasaeed.popularmovies.utilities.MovieConstants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by RamSl-la Saeed on 09-May-18.
 */

public class NetworkUtils {

    public static URL buildUrl(String sortType) {
        Uri builtUri = Uri.parse(MovieConstants.BASE_URL).buildUpon()
                .appendPath(sortType)
                .appendQueryParameter(MovieConstants.API_PARAM, MovieConstants.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    public static URL buildImageUrl(String imageSize,String posterPath) {
        Uri builtUri = Uri.parse(MovieConstants.IMAGE_BASE_URL).buildUpon()
                .appendPath(imageSize)
                .appendPath(posterPath)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}