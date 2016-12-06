package com.example.baryariv.tikaltest.Views.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.baryariv.tikaltest.R;
import com.example.baryariv.tikaltest.models.Movie;
import com.example.baryariv.tikaltest.utils.Defaults;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baryariv on 06/12/2016.
 * <p>
 * This class is the movies adapter - set all movie items in RecyclerView
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private OnMoviesAdapterInteractionListener mListener;

    Cursor dataCursor;
    Context context;

    public List<Movie> moviesDataset;
    public int lastPosition = -1;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public LinearLayout container;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.movie_img);
            container = (LinearLayout) v.findViewById(R.id.container);
        }
    }

    public MoviesAdapter(Activity mContext, Cursor cursor) {
        moviesDataset = new ArrayList<>();
        dataCursor = cursor;
        context = mContext;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_row, parent, false);
        return new ViewHolder(cardview);
    }

    public Cursor swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        dataCursor.moveToPosition(position);

        Picasso.with(context)
                .load(Defaults.PIC_URL + dataCursor.getString(1))
                .fit()
                .error(android.R.drawable.stat_notify_error)
                .into(holder.imageView);

        //On item clicks create the movie model and call openMovieDetails with the movie object
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataCursor.moveToPosition(position);
                Movie selectedMovie = new Movie();
                selectedMovie.setId(dataCursor.getString(3));
                selectedMovie.setOverview(dataCursor.getString(0));
                selectedMovie.setDate(dataCursor.getString(4));
                selectedMovie.setPosterPath(dataCursor.getString(1));
                selectedMovie.setTitle(dataCursor.getString(2));
                selectedMovie.setScore(dataCursor.getString(5));
                mListener.openMovieDetails(selectedMovie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }

    /**
     * Get screen height
     *
     * @return screen height
     */
    public int getScreenHeight() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        return displayMetrics.heightPixels;

    }

    public void setListener(OnMoviesAdapterInteractionListener listener) {
        mListener = listener;
    }

    public interface OnMoviesAdapterInteractionListener {
        void openMovieDetails(Movie selectedMovie);
    }
}
