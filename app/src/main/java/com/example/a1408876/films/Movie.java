package com.example.a1408876.films;

import java.util.Iterator;

import static android.R.id.list;

/**
 * Created by Lenovo on 2017-04-20.
 */

public class Movie {
    //variables for Movie object
    private String title;
    private String overview;
    private String release_date;
    private String original_title;
    private String original_language;
    private String popularity;
    private String vote_average;
    private String vote_count;

    //getters and setters for Movie objects variables
    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getOverview() {

        return overview;
    }

    public void setOverview(String overview) {

        this.overview = overview;
    }

    public String getRelease_date() {

        return release_date;
    }

    public void setRelease_date(String release_date) {

        this.release_date = release_date;
    }

    public String getOriginal_title() {

        return original_title;
    }

    public void setOriginal_title(String original_title) {

        this.original_title = original_title;
    }

    public String getOriginal_language() {

        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getPopularity() {

        return popularity;
    }

    public void setPopularity(String popularity) {

        this.popularity = popularity;
    }

    public String getVote_average() {

        return vote_average;
    }

    public void setVote_average(String vote_average) {

        this.vote_average = vote_average;
    }

    public String getVote_count() {

        return vote_count;
    }

    public void setVote_count(String vote_count) {

        this.vote_count = vote_count;
    }

    //toString method for Movie object
    @Override
    public String toString(){

        return this.getTitle() + "\n" + this.getOverview() + "\n" +
                this.getRelease_date() + "\n" + this.getOriginal_title() +
                "\n" + this.getOriginal_language() + "\n" + this.getPopularity() +
                "\n" + this.getVote_average() + "\n" + this.getVote_count() + "\n";

    }

}
