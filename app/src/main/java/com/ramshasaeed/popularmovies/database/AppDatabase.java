package com.ramshasaeed.popularmovies.database;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ramshasaeed.popularmovies.model.Movie;

@Database(entities = {Favourite.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "popularmovie";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG,"Creating new database");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,AppDatabase.DATABASE_NAME).build();
            }
        }
        Log.d(LOG_TAG,"Getting the Database instance");
        return sInstance;

    }
    public abstract FavouriteDao favouriteDao();
    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
