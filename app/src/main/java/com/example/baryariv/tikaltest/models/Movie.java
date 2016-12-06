package com.example.baryariv.tikaltest.models;

import java.io.Serializable;

/**
 * Created by baryariv on 05/12/2016.
 *
 * This class is the movie model.
 */
public class Movie implements Serializable{

    private String id;
    private String posterPath;
    private String overview;
    private String title;
    private String score;
    private String date;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
