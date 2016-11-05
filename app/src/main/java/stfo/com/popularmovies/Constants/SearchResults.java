package stfo.com.popularmovies.Constants;

/**
 * Created by Kartik Sharma on 08/10/16.
 */
public class SearchResults {
    String poster_path, name;
    int id, type;
    public static final int TYPE_MOVIE = 0, TYPE_PERSON = 1;

    public SearchResults(int id, String poster_path, String name, int type) {
        this.poster_path = poster_path;
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }
}

