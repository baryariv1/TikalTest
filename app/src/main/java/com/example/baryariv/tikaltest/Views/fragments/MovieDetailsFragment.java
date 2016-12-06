package com.example.baryariv.tikaltest.Views.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baryariv.tikaltest.R;
import com.example.baryariv.tikaltest.models.Movie;
import com.example.baryariv.tikaltest.utils.Defaults;
import com.squareup.picasso.Picasso;

/**
 * Created by baryariv on 06/12/2016.
 */
public class MovieDetailsFragment extends Fragment {

    private TextView movieTitle;
    private ImageView movieImg;
    private TextView movieYear;
    private TextView movieOverview;
    private TextView movieScore;

    private Movie movie;


    public MovieDetailsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MoviesFragment.
     */
    public static MovieDetailsFragment newInstance() {
        MovieDetailsFragment fragment = new MovieDetailsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        movieTitle = (TextView) rootView.findViewById(R.id.movie_title);
        movieImg = (ImageView) rootView.findViewById(R.id.movie_img);
        movieYear = (TextView) rootView.findViewById(R.id.movie_year);
        movieOverview = (TextView) rootView.findViewById(R.id.movie_overview);
        movieScore = (TextView) rootView.findViewById(R.id.movie_score);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();
        if (bundle != null){
            movie = (Movie) bundle.getSerializable("selectedMovie");
            setData(movie);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Set the movie details in the view
     *
     * @param movie - movie object with movie details
     */
    public void setData(Movie movie){
        movieTitle.setText(movie.getTitle());
        movieYear.setText(movie.getDate().substring(0, 4));
        movieOverview.setText(movie.getOverview());
        movieScore.setText(movie.getScore() + "/10");

        Picasso.with(getActivity())
                .load(Defaults.PIC_URL + movie.getPosterPath())
                .fit()
                .error(android.R.drawable.stat_notify_error)
                .into(movieImg);
    }
}
