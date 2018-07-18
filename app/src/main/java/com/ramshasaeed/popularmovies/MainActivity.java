package com.ramshasaeed.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelStore;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ramshasaeed.popularmovies.adapter.MoviesAdapter;
import com.ramshasaeed.popularmovies.database.AppDatabase;
import com.ramshasaeed.popularmovies.database.FavouriteDao_Impl;
import com.ramshasaeed.popularmovies.database.FavouriteViewModel;
import com.ramshasaeed.popularmovies.model.Movie;
import com.ramshasaeed.popularmovies.utilities.JSONUtils;
import com.ramshasaeed.popularmovies.utilities.MovieConstants;
import com.ramshasaeed.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridView gvMovies;
    private URL popularMovieURL, topRatedURL;
    private Intent intentDetail;
    private Context context;
    private ArrayList<Movie> movieList;
    private static final String MOVIE_LIST_KEY = "movielist";
    private static final String CURRENT_GRID_VIEW_POSITION = "currentpos";
    private TextView noItem;

    private AppDatabase mDb;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_LIST_KEY, movieList);
        outState.putInt(CURRENT_GRID_VIEW_POSITION, gvMovies.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Toast.makeText(context,"Restore is called",Toast.LENGTH_LONG).show();
        movieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
        if (movieList != null) {
            setMovieAdapter(context, movieList);
        }
        int currentPosition = savedInstanceState.getInt(CURRENT_GRID_VIEW_POSITION);
//        gvMovies.scrollBy(0, currentPosition); //not working
//        gvMovies.smoothScrollToPosition(currentPosition); //not working
        gvMovies.setSelection(currentPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        gvMovies = findViewById(R.id.gv_movies);
        noItem = findViewById(R.id.noItem);
        popularMovieURL = NetworkUtils.buildUrl(MovieConstants.POPULAR_MOVIES_URL);
        topRatedURL = NetworkUtils.buildUrl(MovieConstants.TOP_RATED_MOVIES_URL);
        intentDetail = new Intent(context, MovieDetailActivity.class);

        //movieList = new ArrayList<>();
        //moviesAdapter = new MoviesAdapter(this,movieList);
        mDb = AppDatabase.getInstance(getApplicationContext());
        if (savedInstanceState == null) {
            if (NetworkUtils.isOnline(context)) {
                new MovieService().execute(popularMovieURL);
            } else {
                Toast.makeText(context, "No internet Connection!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_favourite:
                getFavouriteList();
                break;
            case R.id.action_sort_most_popular:
                new MovieService().execute(popularMovieURL);
                noItem.setVisibility(View.GONE);
                break;
            case R.id.action_sort_top_rated:
                new MovieService().execute(topRatedURL);
                noItem.setVisibility(View.GONE);
                break;
        }
        return true;
    }

    private void getFavouriteList() {
        FavouriteViewModel viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(FavouriteViewModel.class);

//        LiveData<List<Movie>> favourites = mDb.favouriteDao().loadAllFavMovies();
//        if (favourites != null && !favourites.equals("")) {
        viewModel.getMovieLists().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                movieList = getMovieList(movies);
                setMovieAdapter(context, movieList);

       if (movies.size() != 0 ) {
            noItem.setVisibility(View.GONE);
        } else {
            noItem.setVisibility(View.VISIBLE);
        }
            }
        });

    }

    private ArrayList<Movie> getMovieList(List<Movie> favourites) {
        ArrayList<Movie> movieList = new ArrayList<>();
        Movie movie;
        for (int i = 0; i < favourites.size(); i++) {
            movie = new Movie();
            movie.setId(favourites.get(i).getId());
            movie.setBackdrop_path(favourites.get(i).getBackdrop_path());
            movie.setOriginal_title(favourites.get(i).getOriginal_title());
            movie.setOverview(favourites.get(i).getOverview());
            movie.setPosterUrl(favourites.get(i).getPosterUrl());
            movie.setRelease_date(favourites.get(i).getRelease_date());
            movie.setVote_average(favourites.get(i).getVote_average());
            movieList.add(movie);
        }
        return movieList;
    }

    public class MovieService extends AsyncTask<URL, ArrayList<Movie>, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String results = null;
            try {
                results = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String resultData) {
            if (resultData != null && !resultData.equals("")) {
                movieList = JSONUtils.parseMovieJson(resultData);
                setMovieAdapter(context, movieList);
            }
        }

    }

    private void setMovieAdapter(Context context, final ArrayList<Movie> movieList) {
        MoviesAdapter moviesAdapter = new MoviesAdapter(context, movieList);
        // Get a reference to the ListView, and attach this adapter to it.
        gvMovies.setAdapter(moviesAdapter);
        gvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie itemMovie = movieList.get(position);
                launchDetailActivity(itemMovie);
            }
        });
    }

    private void launchDetailActivity(Movie itemMovie) {
        intentDetail.putExtra(MovieDetailActivity.MOVIE_INTENT_TAG, itemMovie);
        startActivity(intentDetail);
    }
}
