package com.example.baryariv.tikaltest.Views.activities;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.baryariv.tikaltest.R;
import com.example.baryariv.tikaltest.Views.fragments.MovieDetailsFragment;
import com.example.baryariv.tikaltest.Views.fragments.MoviesFragment;
import com.example.baryariv.tikaltest.models.Movie;
import com.example.baryariv.tikaltest.utils.DBHelper;
import com.example.baryariv.tikaltest.utils.Defaults;
import com.example.baryariv.tikaltest.utils.MoviesContentProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baryariv on 05/12/2016.
 */
public class MainActivity extends AppCompatActivity implements
        MoviesFragment.OnMoviesFragmentInteractionListener {

    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Send network request only if network available
        if (isNetworkAvailable())
            sendRequest();

        //If it's tablet use only landscape mode and add movies fragment
        if (getResources().getBoolean(R.bool.isTablet)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.movies_fragment, MoviesFragment.newInstance())
                    .commit();
            return;
        }

        //If it's phone use only portrait mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, MoviesFragment.newInstance(), "MoviesFragment")
                    .commit();
        }
    }

    /**
     * Send network request to get all movies using Volley library
     */
    private void sendRequest() {
        StringRequest stringRequest = new StringRequest(Defaults.JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MOVIES", response);
                        getMovies(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Get movies list from Json data
     *
     * @param jsonData - movies data
     */
    private void getMovies(String jsonData) {

        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject(jsonData);

            JSONArray results = jsonResponse.getJSONArray("results");

            movies = new ArrayList<>(results.length());

            for (int i = 0; i < results.length(); i++) {

                JSONObject jsonFilm = results.getJSONObject(i);

                Movie movie = new Movie();

                movie.setId(jsonFilm.getString("id"));
                movie.setTitle(jsonFilm.getString("title"));
                movie.setPosterPath(jsonFilm.getString("poster_path"));
                movie.setDate(jsonFilm.getString("release_date"));
                movie.setOverview(jsonFilm.getString("overview"));
                movie.setScore(jsonFilm.getString("vote_average"));

                movies.add(movie);
            }
            saveMovies();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save movies in Sqlite using ContentResolver
     */
    public void saveMovies() {
        for (int i = 0; i < movies.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.MOVIES_COLUMN_ID, movies.get(i).getId());
            values.put(DBHelper.MOVIES_COLUMN_POSTER_PATH, movies.get(i).getPosterPath());
            values.put(DBHelper.MOVIES_COLUMN_DATE, movies.get(i).getDate());
            values.put(DBHelper.MOVIES_COLUMN_OVERVIEW, movies.get(i).getOverview());
            values.put(DBHelper.MOVIES_COLUMN_TITLE, movies.get(i).getTitle());
            values.put(DBHelper.MOVIES_COLUMN_SCORE, movies.get(i).getScore());

            getContentResolver().insert(
                    MoviesContentProvider.CONTENT_URI, values);
        }
    }

    /**
     * Check if network available on the device
     *
     * @return - if network available
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    /**
     * Open selected movie details
     *
     * @param movie - selected movie
     */
    @Override
    public void movieSelected(Movie movie) {
        MovieDetailsFragment detailsFragment = (MovieDetailsFragment) getFragmentManager()
                .findFragmentById(R.id.details_fragment);
        if (detailsFragment != null) {
            detailsFragment.setData(movie);
        } else {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

            MovieDetailsFragment fragment = MovieDetailsFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putSerializable("selectedMovie", movie);
            fragment.setArguments(bundle);

            ft.replace(R.id.container, fragment);
            ft.addToBackStack(null);
            ft.commit();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        super.onBackPressed();

        return true;
    }
}
