package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.databinding.ItemMovieBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    Context context;
    List<Movie> movies;

    ItemMovieBinding itemMovieBinding;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");


        //Original
/*        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);*/

        //View Binding
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        itemMovieBinding = ItemMovieBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemMovieBinding);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);
        // Get the movie at the passed in position
        Movie movie = movies.get(position);
        // Bind the movie data into the VH
        holder.bind(movie);

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemMovieBinding binding;

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        // used for reducing view boilerplate

        public ViewHolder(@NonNull ItemMovieBinding itemView) {
            //super(itemView);      //Instead of this
            super(itemView.getRoot());
            this.binding = itemView;

            itemView.getRoot().setOnClickListener(this);


            /*
            ivPoster = (ImageView) itemView.findViewById(R.id.ivPoster);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);*/
            // add this as the itemView's OnClickListener

            /*
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
             */
        }

        // when the user clicks on a row, show MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }

        public void bind(Movie movie) {
            binding.tvTitle.setText(movie.getTitle());
            binding.tvOverview.setText(movie.getOverview());

            String imageUrl;
            int placeholderID;

            // if phone is in landscape
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // then imageUrl = backdrop image
                imageUrl = movie.getBackdropPath();
                placeholderID = R.drawable.flicks_backdrop_placeholder;
            } else {
                // else imageUrl = poster image
                imageUrl = movie.getPosterPath();
                placeholderID = R.drawable.flicks_movie_placeholder;
            }

            // Adds a placeholder image
            Glide.with(context).load(imageUrl)
                    .placeholder(placeholderID)
                    .centerCrop()
                    .transform(new RoundedCorners(50))
                    .into(binding.ivPoster);

        }
    }
}
