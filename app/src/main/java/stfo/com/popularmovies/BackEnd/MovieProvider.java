package stfo.com.popularmovies.BackEnd;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Toast;

import stfo.com.popularmovies.Constants.Review;

/**
 * Created by Kartik Sharma on 22/09/16.
 */
public class MovieProvider extends ContentProvider {

    static final int FAVOURITE = 100;
    static final int MOVIE = 200;
    static final int CAST_MOVIE = 300;
    private static final UriMatcher uriMatcher = buildUriMatcher();
    static UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContracts.CONTENT_AUTHORITY,MovieContracts.PATH_MOVIES,FAVOURITE);
        uriMatcher.addURI(MovieContracts.CONTENT_AUTHORITY,MovieContracts.PATH_MOVIES + "/#",MOVIE);
        uriMatcher.addURI(MovieContracts.CONTENT_AUTHORITY,MovieContracts.PATH_CASTS + "/#", CAST_MOVIE);
        return uriMatcher;
    }

    private MovieDbHelper dbHelper;
    private Context context;

    @Override
    public boolean onCreate() {
        context = getContext();
        dbHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor returnCursor = null;
        String id = uri.getLastPathSegment();
        switch (uriMatcher.match(uri)) {
            case FAVOURITE:
                returnCursor = getFavourites();
                break;
            case MOVIE:
                returnCursor = getMovie(id);
                break;
            case CAST_MOVIE:
                returnCursor = getCast_Movie(id);
                break;
        }
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri returnUri = null;
        long id = 0 ;
        switch (uriMatcher.match(uri)){
            case FAVOURITE :
            case MOVIE :
                id = addMovie(values);
                if(id!=0)
                    returnUri = MovieContracts.MovieEntry.buildMovieUri(id);
                break;
            case CAST_MOVIE :
                id = addCast(values);
                if(id!=0)
                    returnUri = MovieContracts.CastEntry.buildCastUriForMovie(id);
                break;
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if(uriMatcher.match(uri) == MOVIE ){
            return deleteMovie(uri.getLastPathSegment());
        }else if(uriMatcher.match(uri) == CAST_MOVIE){
            return deleteCast(uri.getLastPathSegment());
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private Cursor getFavourites(){
        return dbHelper.getReadableDatabase().query(true, MovieContracts.MovieEntry.TABLE_NAME,null,
                null, null, null,null,null,null);
    }

    private int deleteMovie(String id){
        return dbHelper.getWritableDatabase().delete(MovieContracts.MovieEntry.TABLE_NAME,
                MovieContracts.MovieEntry._ID+"=?",new String[]{id});
    }


    private Cursor getMovie(String id){
        return dbHelper.getReadableDatabase().query(true, MovieContracts.MovieEntry.TABLE_NAME,null,
                MovieContracts.MovieEntry._ID + "=?", new String[]{id}, null,null,null,null);
    }

    private long addMovie(ContentValues values){
        return dbHelper.getWritableDatabase().insert(MovieContracts.MovieEntry.TABLE_NAME,null, values);
    }

    private Cursor getCast_Movie(String movieId){
        String[] columns = {MovieContracts.CastEntry.COLUMN_NAME, MovieContracts.CastEntry.COLUMN_CHARACTER,
                MovieContracts.CastEntry.COLUMN_IMAGEPATH};
        return dbHelper.getReadableDatabase().query(true,MovieContracts.CastEntry.TABLE_NAME,columns,
                MovieContracts.CastEntry.COLUMN_MOVIE_ID+"=?",new String[]{movieId},null,null,null,null);
    }

    private long addCast(ContentValues values){
        return dbHelper.getWritableDatabase().insert(MovieContracts.CastEntry.TABLE_NAME,null,values);
    }

    private int deleteCast(String id){
        return dbHelper.getWritableDatabase().delete(MovieContracts.CastEntry.TABLE_NAME,
                MovieContracts.CastEntry.COLUMN_MOVIE_ID+"=?",new String[]{id});
    }

    @Override
    public void shutdown() {
        dbHelper.close();
        super.shutdown();
    }

}
