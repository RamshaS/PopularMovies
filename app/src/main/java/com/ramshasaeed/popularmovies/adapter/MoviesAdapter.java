package com.ramshasaeed.popularmovies.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.ramshasaeed.popularmovies.utilities.NetworkUtils;
import com.ramshasaeed.popularmovies.R;
import com.ramshasaeed.popularmovies.model.Movie;
import com.ramshasaeed.popularmovies.utilities.MovieConstants;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by RamSl-la Saeed on 10-May-18.
 */

public class MoviesAdapter extends ArrayAdapter<Movie> {
    public MoviesAdapter(@NonNull Context context, List<Movie> movieList) {
        super(context, 0,movieList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);
        if(convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_adapter,parent,false);
        }
        ImageView imageView = convertView.findViewById(R.id.movie_image);
        String posterPath = movie.getPosterUrl();

        URL posterURL = NetworkUtils.buildImageUrl(MovieConstants.IMAGE_SIZE_w185,posterPath.replace("/",""));

        Picasso.with(parent.getContext())
                .load(String.valueOf(posterURL))
                .error(android.R.drawable.ic_dialog_alert)
                .into(imageView);

        return convertView;
    }
}
