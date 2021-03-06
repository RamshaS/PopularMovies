package com.ramshasaeed.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramshasaeed.popularmovies.adapter.ReviewAdapter;
import com.ramshasaeed.popularmovies.adapter.TrailerAdapter;
import com.ramshasaeed.popularmovies.database.AddFavouriteVieModel;
import com.ramshasaeed.popularmovies.database.AddFavouriteViewModelFactory;
import com.ramshasaeed.popularmovies.database.AppDatabase;
import com.ramshasaeed.popularmovies.database.AppExecutors;
import com.ramshasaeed.popularmovies.model.Movie;
import com.ramshasaeed.popularmovies.model.Reviews;
import com.ramshasaeed.popularmovies.model.Trailers;
import com.ramshasaeed.popularmovies.utilities.JSONUtils;
import com.ramshasaeed.popularmovies.utilities.MovieConstants;
import com.ramshasaeed.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListner {
    final static String MOVIE_INTENT_TAG = "movie";
    Context context = MovieDetailActivity.this;
    private Movie movie;
    private TextView tvMovieTitle, tvReleaseDate, tvRating, tvOverview;
    private ImageView imgMovie, imgFavourite;
    private RecyclerView rvReviews, rvTrailers;
    private URL movieReviewsURL, movieTrailersURL;
    private ArrayList<Reviews> reviewsList;
    private ArrayList<Trailers> trailersList;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        tvMovieTitle = findViewById(R.id.tv_movie_title);
        tvReleaseDate = findViewById(R.id.tv_release_date);
        tvOverview = findViewById(R.id.tv_overview);
        tvRating = findViewById(R.id.tv_rating);
        imgMovie = findViewById(R.id.img_movie);
        imgFavourite = findViewById(R.id.img_favourite);
        rvReviews = findViewById(R.id.rv_reviews);
        rvTrailers = findViewById(R.id.rv_trailers);

        mDb = AppDatabase.getInstance(getApplicationContext());

        getMovieFromIntent();

        movieReviewsURL = NetworkUtils.buildUrl(movie.getId(), MovieConstants.MOVIE_REVIEWS_URL);
        movieTrailersURL = NetworkUtils.buildUrl(movie.getId(), MovieConstants.MOVIE_TRAILERS_URL);
        initFavoriteBtn();
        getReviews();
        getTrailers();
    }


    private void getReviews() {
        new ReviewService().execute(movieReviewsURL);
    }

    private void getTrailers() {
        new TrailerService().execute(movieTrailersURL);
    }

    private void initFavoriteBtn() {
        imgFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movie.isFavorite()) {
                    setMovieAsFavorite(true);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.favouriteDao().insertFavourite(movie);
                        }
                    });
                } else {
                    setMovieAsFavorite(false);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.favouriteDao().deleteFavMovie(movie);
                        }
                    });
                }
            }
        });
    }

    private void getMovieFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(MOVIE_INTENT_TAG)) {
            movie = intent.getParcelableExtra(MOVIE_INTENT_TAG);
//            LiveData<Movie> movieLiveData = mDb.favouriteDao().loadFavoriteById(movie.getId());

            AddFavouriteViewModelFactory factory = new AddFavouriteViewModelFactory(mDb,movie.getId());
            ViewModelProvider provider = new ViewModelProvider(this,factory);
            AddFavouriteVieModel model = provider.get(AddFavouriteVieModel.class);
            model.getMovieLiveData().observe(this, new Observer<Movie>() {
                @Override
                public void onChanged(@Nullable Movie movie) {
                    setMovieAsFavorite( movie != null);
                    populateUI();
                }
            });

        }
    }

    private void setMovieAsFavorite(boolean isFavorite) {
        movie.setFavorite(isFavorite);
        if (isFavorite) {
            imgFavourite.setColorFilter(ContextCompat.getColor(context, android.R.color.holo_red_light));
        } else {
            imgFavourite.setColorFilter(ContextCompat.getColor(context, android.R.color.darker_gray));
        }
    }

    private void populateUI() {
        tvMovieTitle.setText(movie.getOriginal_title());
        String voteAverage = movie.getVote_average() + "/10";
        tvRating.setText(voteAverage);
        tvOverview.setText(movie.getOverview());
        String[] splitDate = movie.getRelease_date().split("-");
        tvReleaseDate.setText(splitDate[0]);
        String posterPath = movie.getPosterUrl();
        URL posterURL = NetworkUtils.buildImageUrl(MovieConstants.IMAGE_SIZE_w342, posterPath.replace("/", ""));

        Picasso.with(this)
                .load(String.valueOf(posterURL))
                .error(android.R.drawable.ic_dialog_alert)
                .into(imgMovie);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        String key = trailersList.get(clickedItemIndex).getKey();
        watchYoutubeVideo(this, key);
        //        Toast.makeText(this, "Key: " + key, Toast.LENGTH_SHORT).show();
    }

    public class ReviewService extends AsyncTask<URL, ArrayList<Reviews>, String> {

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
                reviewsList = JSONUtils.parseReviewsJson(resultData);

                setReviewAdapter(context, reviewsList);
            }
        }
    }

    public class TrailerService extends AsyncTask<URL, ArrayList<Trailers>, String> {

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
                trailersList = JSONUtils.parseTrailerJson(resultData);

                setTrailerAdapter(context, trailersList);
            }
        }

    }

    private void setReviewAdapter(Context context, final ArrayList<Reviews> reviewsList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvReviews.setLayoutManager(layoutManager);
        ReviewAdapter reviewsAdapter = new ReviewAdapter(context, reviewsList);
        // Get a reference to the ListView, and attach this adapter to it.
        rvReviews.setAdapter(reviewsAdapter);
    }

    private void setTrailerAdapter(Context context, final ArrayList<Trailers> trailersList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTrailers.setLayoutManager(layoutManager);
        TrailerAdapter trailerAdapter = new TrailerAdapter(context, trailersList, this);
        // Get a reference to the ListView, and attach this adapter to it.
        rvTrailers.setAdapter(trailerAdapter);
    }

    public static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, NetworkUtils.buildYoutubeUrl(id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
