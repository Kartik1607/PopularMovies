package stfo.com.popularmovies.Constants;

/**
 * Created by Kartik Sharma on 04/10/16.
 */
public class MovieCredits {
    String movie_name, character, image_path;
    int movie_id;

    public MovieCredits(int movie_id, String movie_name, String character, String image_path) {
        this.movie_id = movie_id;
        this.movie_name = movie_name;
        this.character = character;
        this.image_path = image_path;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public String getCharacter() {
        return character;
    }

    public String getImage_path() {
        return image_path;
    }
}
