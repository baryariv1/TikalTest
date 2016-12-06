package com.example.baryariv.tikaltest.Views.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baryariv.tikaltest.R;
import com.example.baryariv.tikaltest.Views.adapters.MoviesAdapter;
import com.example.baryariv.tikaltest.models.Movie;
import com.example.baryariv.tikaltest.utils.DBHelper;
import com.example.baryariv.tikaltest.utils.MoviesContentProvider;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoviesFragment.OnMoviesFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
        , MoviesAdapter.OnMoviesAdapterInteractionListener {
    private RecyclerView recyclerView;
    MoviesAdapter adapter;
    private int LOADER_ID = 1;

    private OnMoviesFragmentInteractionListener mListener;

    public MoviesFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MoviesFragment.
     */
    public static MoviesFragment newInstance() {
        MoviesFragment fragment = new MoviesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler);
        recyclerView.setHasFixedSize(true);
        if (getResources().getBoolean(R.bool.isTablet))
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));


        adapter = new MoviesAdapter(getActivity(), null);
        adapter.setListener(new MoviesAdapter.OnMoviesAdapterInteractionListener() {
            @Override
            public void openMovieDetails(Movie selectedMovie) {
                mListener.movieSelected(selectedMovie);
            }
        });
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMoviesFragmentInteractionListener) {
            mListener = (OnMoviesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMoviesFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnMoviesFragmentInteractionListener) {
            mListener = (OnMoviesFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnMoviesFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {DBHelper.MOVIES_COLUMN_OVERVIEW, DBHelper.MOVIES_COLUMN_POSTER_PATH,
                DBHelper.MOVIES_COLUMN_TITLE, DBHelper.MOVIES_COLUMN_ID, DBHelper.MOVIES_COLUMN_DATE, DBHelper.MOVIES_COLUMN_SCORE};
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                MoviesContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void openMovieDetails(Movie selectedMovie) {
        mListener.movieSelected(selectedMovie);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMoviesFragmentInteractionListener {
        void movieSelected(Movie movie);
    }
}
