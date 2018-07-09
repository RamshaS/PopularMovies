package com.ramshasaeed.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ramshasaeed.popularmovies.model.Movie;

import java.util.List;

@Dao
public interface FavouriteDao {
    @Query("SELECT * FROM favourite")
    LiveData<List<Favourite>> loadAllFavMovies();

    @Insert
    void insertFavourite(Favourite favourite);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavourite(Favourite favourite);

    @Delete
    void deleteFavMovie(Favourite favourite);

    @Query("SELECT * FROM favourite WHERE id = :id")
    LiveData<Favourite> loadTaskById(int id);
}
