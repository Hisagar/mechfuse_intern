package com.example.interntask;

import org.json.JSONArray;

public class Movie {
    Double popularity;
    int vote_count;
    String poster_path;
    int id;
   boolean adult;
    String original_language;
    String original_title;
    Double vote_average;
    String overview;
    String relese_date;
    JSONArray genre_ids;

    public Movie(Double popularity, int vote_count, String poster_path, int id, boolean adult, String original_language, String original_title, Double vote_average, String overview, String relese_date, JSONArray genre_ids) {
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.poster_path = poster_path;
        this.id = id;
        this.adult = adult;
        this.original_language = original_language;
        this.original_title = original_title;
        this.vote_average = vote_average;
        this.overview = overview;
        this.relese_date = relese_date;
        this.genre_ids = genre_ids;
    }

    public Movie() { }

    public JSONArray getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(JSONArray genre_ids) {
        this.genre_ids = genre_ids;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelese_date() {
        return relese_date;
    }

    public void setRelese_date(String relese_date) {
        this.relese_date = relese_date;
    }
}
