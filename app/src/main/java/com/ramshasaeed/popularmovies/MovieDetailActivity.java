package com.ramshasaeed.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramshasaeed.popularmovies.model.Movie;
import com.ramshasaeed.popularmovies.utilities.MovieConstants;
import com.ramshasaeed.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {
    final static String MOVIE_INTENT_TAG = "movie";
    private Movie movie;
    private TextView tvMovieTitle, tvReleaseDate, tvRating, tvOverview;
    private ImageView imgMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movie = new Movie();
        tvMovieTitle = findViewById(R.id.tv_movie_title);
        tvReleaseDate = findViewById(R.id.tv_release_date);
        tvOverview = findViewById(R.id.tv_overview);
        tvRating = findViewById(R.id.tv_rating);
        imgMovie = findViewById(R.id.img_movie);
        gettingIntents();

    }

    private void gettingIntents() {
        Intent intent = getIntent();
        if (intent.hasExtra(MOVIE_INTENT_TAG)) {
            movie = intent.getParcelableExtra(MOVIE_INTENT_TAG);
            populateUI();

        }
    }

    private void populateUI() {
        tvMovieTitle.setText(movie.getOriginal_title());
        String voteAverage = movie.getVote_average()+"/10";
        tvRating.setText(voteAverage);
        tvOverview.setText(movie.getOverview());
        String[] splitDate = movie.getRelease_date().split("-");
        tvReleaseDate.setText(splitDate[0]);
        String posterPath = movie.getPosterUrl();
        URL posterURL = NetworkUtils.buildImageUrl(MovieConstants.IMAGE_SIZE_w342,posterPath.replace("/",""));

        Picasso.with(this)
                .load(String.valueOf(posterURL))
                .error(android.R.drawable.ic_dialog_alert)
                .into(imgMovie);
    }
}
