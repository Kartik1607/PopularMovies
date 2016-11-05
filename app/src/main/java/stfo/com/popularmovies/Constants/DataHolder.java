package stfo.com.popularmovies.Constants;

import java.util.ArrayList;

public class DataHolder {


    private String poster_path, backdrop_path, title, release_date, overview, tagline;
    private int id, vote_count, runtime;
    private double rating;
    private ArrayList<Review> reviews;
    private ArrayList<Casts> casts;
    private ArrayList<Trailer> trailers;
    private ArrayList<String> gallery;

    public DataHolder() {
    }

    public void clear(){
        poster_path = backdrop_path = title = release_date = overview = tagline = "";
        vote_count = runtime = 0;
        rating = 0;
        reviews = null;
        casts = null;
        trailers = null;
        gallery = null;
    }


    public synchronized  ArrayList<Trailer> getTrailers() {
        return trailers;
    }


    public synchronized  ArrayList<String> getGallery() {
        return gallery;
    }

    public synchronized DataHolder setTrailer(ArrayList<Trailer> trailers){
        this.trailers = trailers;
        return this;
    }

    public synchronized DataHolder setGallery(ArrayList<String> gallery){
        this.gallery = gallery;
        return this;
    }

    public synchronized DataHolder setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
        return this;
    }



    public synchronized DataHolder setRuntime(int runtime) {
        this.runtime = runtime;
        return this;
    }

    public synchronized DataHolder setPoster_path(String poster_path) {
        this.poster_path = poster_path;
        return this;
    }

    public synchronized DataHolder setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
        return this;
    }

    public synchronized DataHolder setTitle(String title) {
        this.title = title;
        return this;
    }

    public synchronized DataHolder setRelease_date(String release_date) {
        this.release_date = release_date;
        return this;
    }

    public synchronized DataHolder setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public synchronized DataHolder setTagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public synchronized DataHolder setId(int id) {
        this.id = id;
        return this;
    }

    public synchronized DataHolder setVote_count(int vote_count) {
        this.vote_count = vote_count;
        return this;
    }

    public synchronized DataHolder setRating(double rating) {
        this.rating = rating;
        return this;
    }

    public synchronized DataHolder setCasts(ArrayList<Casts> casts){
        this.casts = casts;
        return this;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getTitle() {
        return title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getOverview() {
        return overview;
    }

    public int getId() {
        return id;
    }

    public int getVote_count() {
        return vote_count;
    }

    public String getTagline() {
        return  tagline;
    }

    public double getRating() {
        return rating;
    }

    public int getRuntime() {
        return runtime;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public ArrayList<Casts> getCasts(){
        return casts;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DataHolder){
            return ((DataHolder)obj).id == id;
        }
        return false;
    }
}