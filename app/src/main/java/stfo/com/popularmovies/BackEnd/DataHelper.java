package stfo.com.popularmovies.BackEnd;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import stfo.com.popularmovies.Constants.Casts;
import stfo.com.popularmovies.Constants.DataHolder;
import stfo.com.popularmovies.Constants.Review;

/**
 * Created by Kartik Sharma on 27/09/16.
 */
public class DataHelper {

    public static Cursor getFavourites(ContentResolver contentResolver){
        Uri favUri = MovieContracts.MovieEntry.buildMovieUri();
        return contentResolver.query(favUri,null,null,null,null);
    }

    public static int deleteMovie(ContentResolver contentResolver, int movieId){
        Uri movieUri = MovieContracts.MovieEntry.buildMovieUri(movieId);
        return contentResolver.delete(movieUri,null,null);
    }

    public static int deleteCast(ContentResolver contentResolver, int movieId){
        Uri castUri = MovieContracts.CastEntry.buildCastUriForMovie(movieId);
        return contentResolver.delete(castUri,null,null);
    }
    public static Uri insertMovie(ContentResolver contentResolver, DataHolder holder){
        Uri movieUri = MovieContracts.MovieEntry.buildMovieUri(holder.getId());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContracts.MovieEntry._ID,holder.getId());
        contentValues.put(MovieContracts.MovieEntry.COLUMN_MOVIE_NAME,holder.getTitle());
        contentValues.put(MovieContracts.MovieEntry.COLUMN_MOVIE_TAGLINE,holder.getTagline());
        contentValues.put(MovieContracts.MovieEntry.COLUMN_MOVIE_RUNTIME,holder.getRuntime());
        contentValues.put(MovieContracts.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,holder.getRelease_date());
        contentValues.put(MovieContracts.MovieEntry.COLUMN_MOVIE_OVERVIEW,holder.getOverview());
        contentValues.put(MovieContracts.MovieEntry.COLUMN_MOVIE_AVERAGE,holder.getRating());
        contentValues.put(MovieContracts.MovieEntry.COLUMN_MOVIE_VOTES,holder.getVote_count());
        contentValues.put(MovieContracts.MovieEntry.COLUMN_MOVIE_POSTER,holder.getPoster_path());
        contentValues.put(MovieContracts.MovieEntry.COLUMN_MOVIE_BACKDROP,holder.getBackdrop_path());
        return contentResolver.insert(movieUri, contentValues);
    }

    public static Cursor getMovie(ContentResolver contentResolver ,int id){
        Uri movieUri = MovieContracts.MovieEntry.buildMovieUri(id);
        return contentResolver.query(movieUri,null,null,null,null);
    }

    public static Uri insertCastforMovie(ContentResolver contentResolver, int movieId, Casts cast){
        Uri castMovieUri = MovieContracts.CastEntry.buildCastUriForMovie(movieId);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContracts.CastEntry.COLUMN_MOVIE_ID,movieId);
        contentValues.put(MovieContracts.CastEntry.COLUMN_NAME,cast.getName());
        contentValues.put(MovieContracts.CastEntry.COLUMN_CHARACTER,cast.getRole());
        contentValues.put(MovieContracts.CastEntry.COLUMN_IMAGEPATH,cast.getImagePath());
        return contentResolver.insert(castMovieUri,contentValues);
    }
    public static Cursor getCastforMovie(ContentResolver contentResolver, int movieId){
        Uri castMovieUri = MovieContracts.CastEntry.buildCastUriForMovie(movieId);
        return contentResolver.query(castMovieUri,null,null,null,null);
    }

}
