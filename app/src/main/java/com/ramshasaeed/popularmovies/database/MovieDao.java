package com.ramshasaeed.popularmovies.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ramshasaeed.popularmovies.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

@Query("SELECT * FROM  movie")
    List<Movie> loadAllMovies();

@Insert
    void insertMovie(Movie movie);

@Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

@Delete
    void deleteMovie(Movie movie);


}
