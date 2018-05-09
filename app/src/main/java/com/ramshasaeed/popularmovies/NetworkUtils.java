package com.ramshasaeed.popularmovies;

/**
 * Created by RamSl-la Saeed on 09-May-18.
 */

public class NetworkUtils {
    private static final NetworkUtils ourInstance = new NetworkUtils();

    public static NetworkUtils getInstance() {
        return ourInstance;
    }

    private NetworkUtils() {
    }
}
