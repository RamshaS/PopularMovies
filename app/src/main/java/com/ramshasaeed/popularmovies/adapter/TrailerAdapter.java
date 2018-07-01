package com.ramshasaeed.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ramshasaeed.popularmovies.R;
import com.ramshasaeed.popularmovies.model.Trailers;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {

    ArrayList<Trailers> trailersList;
    Context context;
private final ListItemClickListner mOnClickListener;
    public TrailerAdapter( Context context, ArrayList<Trailers> trailersList, ListItemClickListner mOnClickListener) {
        this.trailersList = trailersList;
        this.context = context;
        this.mOnClickListener = mOnClickListener;
    }
    public interface ListItemClickListner{
void onListItemClick(int clickedItemIndex);
    }
    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_adapter,parent,false);
        return new TrailerHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return trailersList.size() > 0? trailersList.size():0 ;
    }


    public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvName;

        public TrailerHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(this);
        }

        void bind(int position){
            tvName.setText(trailersList.get(position).getName());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
