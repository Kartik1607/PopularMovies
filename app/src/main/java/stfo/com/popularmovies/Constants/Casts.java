package stfo.com.popularmovies.Constants;

import java.util.ArrayList;

/**
 * Created by Kartik Sharma on 17/09/16.
 */
public class Casts extends Person{
    private String role;
    private ArrayList<MovieCredits> credits;

    public Casts(int id, String name, String role, String imagePath) {
        super(id,name,imagePath);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setMovieCredits(ArrayList<MovieCredits> credits){
        this.credits = credits;
    }

    public ArrayList<MovieCredits> getCredits() {
        return credits;
    }
}
