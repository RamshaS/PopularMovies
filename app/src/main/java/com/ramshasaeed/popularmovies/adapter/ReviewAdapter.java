package com.ramshasaeed.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ramshasaeed.popularmovies.R;
import com.ramshasaeed.popularmovies.model.Reviews;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder>{

private ArrayList<Reviews> reviewsList;
private Context context;

    public ReviewAdapter( Context context,ArrayList<Reviews> reviewsList) {
        this.reviewsList = reviewsList;
        this.context = context;
    }

    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_adapter,parent,false);
        return new ReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return reviewsList.size() > 0 ? reviewsList.size() : 0;
    }

    public class ReviewHolder extends RecyclerView.ViewHolder{
        TextView tvAuthor, tvContent;
        public ReviewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
        void bind(int position){
            tvAuthor.setText(reviewsList.get(position).getAuthor());
            tvContent.setText(reviewsList.get(position).getContent());

        }
    }
}
