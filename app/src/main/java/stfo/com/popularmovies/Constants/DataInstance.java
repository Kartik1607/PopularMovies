package stfo.com.popularmovies.Constants;

import java.util.ArrayList;

/**
 * Created by Kartik Sharma on 16/09/16.
 */
public class DataInstance {

    private static ArrayList<DataHolder> instance;
    private static DataHolder tempInstance;

    public static synchronized ArrayList<DataHolder> getInstance(){
        if(instance == null)
            instance = new ArrayList<>();
        return instance;
    }

    public static synchronized DataHolder getTempInstance(){
        if(tempInstance == null)
            tempInstance = new DataHolder();
        return tempInstance;
    }
}
