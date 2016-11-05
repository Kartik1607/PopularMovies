package stfo.com.popularmovies.BackEnd;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Kartik Sharma on 22/09/16.
 */
public final class MovieContracts {

    public static final String CONTENT_AUTHORITY = "stfo.com.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "Movie";
    public static final String PATH_CASTS = "Casts";

    private MovieContracts(){}

    public static final class MovieEntry implements BaseColumns{
        private static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_NAME = "column_movie_name";
        public static final String COLUMN_MOVIE_TAGLINE = "column_movie_tagline";
        public static final String COLUMN_MOVIE_RUNTIME = "column_movie_runtime";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "column_movie_release_date";
        public static final String COLUMN_MOVIE_AVERAGE = "column_movie_average";
        public static final String COLUMN_MOVIE_VOTES = "column_movie_votes";
        public static final String COLUMN_MOVIE_OVERVIEW = "column_movie_overvieww";
        public static final String COLUMN_MOVIE_POSTER = "column_movie_poster";
        public static final String COLUMN_MOVIE_BACKDROP = "column_movie_backdrop";

        //GET MOVIE WITH MOVIE ID
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //GET ALL MOVIES
        public static Uri buildMovieUri() {
            return CONTENT_URI;
        }
    }


    public static final class CastEntry implements BaseColumns{
        private static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CASTS).build();
        public static final String TABLE_NAME = "casts";

        public static final String COLUMN_MOVIE_ID = "column_movie_id";
        public static final String COLUMN_CHARACTER = "column_character";
        public static final String COLUMN_NAME = "column_name";
        public static final String COLUMN_IMAGEPATH = "column_imagepath";


        //GETS ALL THE CAST FOR MOVIE ID
        public static Uri buildCastUriForMovie(long id) {
            return CONTENT_URI.buildUpon().appendPath(id + "").build();
        }
    }
}
