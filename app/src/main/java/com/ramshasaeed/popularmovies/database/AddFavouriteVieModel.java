package com.ramshasaeed.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.ramshasaeed.popularmovies.model.Movie;

public class AddFavouriteVieModel extends ViewModel {
    private LiveData<Movie> movieLiveData;

    public AddFavouriteVieModel(AppDatabase db, int movieId) {
            movieLiveData = db.favouriteDao().loadFavoriteById(movieId);
    }

    public LiveData<Movie> getMovieLiveData() {
        return movieLiveData;
    }
}
