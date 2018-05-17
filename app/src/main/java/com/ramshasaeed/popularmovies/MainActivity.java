package com.ramshasaeed.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ramshasaeed.popularmovies.adapter.MoviesAdapter;
import com.ramshasaeed.popularmovies.model.Movie;
import com.ramshasaeed.popularmovies.utilities.JSONUtils;
import com.ramshasaeed.popularmovies.utilities.MovieConstants;
import com.ramshasaeed.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView gvMovies;
    URL populaMovieURL, topRatedURL;
    Intent intentDetail;
    Context context;
    ArrayList<Movie> movieList;
    private static final String MOVIE_LIST_KEY = "movielist";
    MoviesAdapter moviesAdapter;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_LIST_KEY, movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Toast.makeText(context,"Restore is called",Toast.LENGTH_LONG).show();
        movieList= savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
        setMovieAdapter(context,movieList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        gvMovies = findViewById(R.id.gv_movies);
        populaMovieURL = NetworkUtils.buildUrl(MovieConstants.POPULAR_MOVIES_URL);
        topRatedURL = NetworkUtils.buildUrl(MovieConstants.TOP_RATED_MOVIES_URL);
        intentDetail = new Intent(context, MovieDetailActivity.class);
        //movieList = new ArrayList<>();
        //moviesAdapter = new MoviesAdapter(this,movieList);
        if (savedInstanceState == null) {
            if (NetworkUtils.isOnline(context)) {
                new MovieService().execute(populaMovieURL);
            }else {
                Toast.makeText(context,"No internet Connection!",Toast.LENGTH_LONG).show();
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
            case R.id.action_sort_most_popular:
                new MovieService().execute(populaMovieURL);
                break;
            case R.id.action_sort_top_rated:
                new MovieService().execute(topRatedURL);
                break;
        }
        return true;
    }

    public class MovieService extends AsyncTask<URL, ArrayList<Movie>, String> {
        String TAG = MovieService.class.getName();

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
                setMovieAdapter(context,movieList);
            }
        }

    }
    public void setMovieAdapter(Context context, final ArrayList<Movie> movieList){
        moviesAdapter = new MoviesAdapter(context, movieList);
        // Get a reference to the ListView, and attach this adapter to it.
        gvMovies.setAdapter(moviesAdapter);
        gvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie itemMoview = movieList.get(position);
                launchDetailActivity(itemMoview);
            }
        });
    }

    private void launchDetailActivity( Movie itemMoview) {
        intentDetail.putExtra(MovieDetailActivity.MOVIE_INTENT_TAG,(Parcelable) itemMoview);
        startActivity(intentDetail);
    }
}
