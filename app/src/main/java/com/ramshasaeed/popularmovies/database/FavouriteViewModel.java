package com.ramshasaeed.popularmovies.database;

import android.app.Application;
import android.app.ListActivity;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ramshasaeed.popularmovies.model.Movie;

import java.util.List;

public class FavouriteViewModel extends AndroidViewModel {
private LiveData<List<Movie>> movieLists;
    public FavouriteViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        movieLists = db.favouriteDao().loadAllFavMovies();
    }

    public LiveData<List<Movie>> getMovieLists() {
        return movieLists;
    }
}
