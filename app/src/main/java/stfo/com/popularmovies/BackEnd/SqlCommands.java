package stfo.com.popularmovies.BackEnd;

/**
 * Created by Kartik Sharma on 22/09/16.
 */

import java.security.PublicKey;

import static stfo.com.popularmovies.BackEnd.MovieContracts.*;


public final class SqlCommands {

    public static final String SQL_CREATE_TABLE_MOVIE =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " ( " +
                    MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                    MovieEntry.COLUMN_MOVIE_NAME + " VARCHAR, " +
                    MovieEntry.COLUMN_MOVIE_TAGLINE + " VARCHAR, " +
                    MovieEntry.COLUMN_MOVIE_RUNTIME + " INTEGER, " +
                    MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " DATE, " +
                    MovieEntry.COLUMN_MOVIE_AVERAGE + " DECIMAL, " +
                    MovieEntry.COLUMN_MOVIE_VOTES + " INTEGER, " +
                    MovieEntry.COLUMN_MOVIE_OVERVIEW + " VARCHAR, " +
                    MovieEntry.COLUMN_MOVIE_POSTER + " VARCHAR, " +
                    MovieEntry.COLUMN_MOVIE_BACKDROP + " VARCHAR " +
                    " );" ;


    public static final String SQL_CREATE_TABLE_CASTS =
            "CREATE TABLE " + CastEntry.TABLE_NAME + " ( " +
                    CastEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                    CastEntry.COLUMN_NAME + " VARCHAR, " +
                    CastEntry.COLUMN_CHARACTER + " VARCHAR,"  +
                    CastEntry.COLUMN_IMAGEPATH + " VARCHAR " +
                    " ); ";

    public static final String SQL_DROP_TABLE_MOVIE = "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;
    public static final String SQL_DROP_TABLE_CASTS = "DROP TABLE IF EXISTS " + CastEntry.TABLE_NAME;

}
