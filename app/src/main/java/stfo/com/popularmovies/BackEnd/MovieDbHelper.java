package stfo.com.popularmovies.BackEnd;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kartik Sharma on 22/09/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;

    static final String DB_NAME = "pop_movies.db";

    public MovieDbHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlCommands.SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SqlCommands.SQL_CREATE_TABLE_CASTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SqlCommands.SQL_DROP_TABLE_MOVIE);
        db.execSQL(SqlCommands.SQL_DROP_TABLE_CASTS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}